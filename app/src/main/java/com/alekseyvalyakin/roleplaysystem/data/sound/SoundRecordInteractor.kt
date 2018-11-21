package com.alekseyvalyakin.roleplaysystem.data.sound

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Process
import com.alekseyvalyakin.roleplaysystem.data.sound.Format3GP.FileEncoder
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.alekseyvalyakin.roleplaysystem.utils.file.FileInfoProvider
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.*
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import timber.log.Timber
import java.io.File

class SoundRecordInteractorImpl(
        private val fileInfoProvider: FileInfoProvider
) : SoundRecordInteractor {

    private val relay = BehaviorRelay.createDefault<RecordInfo>(RecordInfo())!!
    private var recordDisposable = Disposables.disposed()
    private var saveRecordDisposable = Disposables.disposed()
    private val sampleRate = RawSamples.SAMPLE_RATE

    @Synchronized
    override fun startRecordFile(gameId: String) {
        if (!recordDisposable.isDisposed) {
            return
        }

        val value = relay.value!!
        val recordsTempDir = fileInfoProvider.getRecordsTempDir()
        recordsTempDir.mkdirs()


        val fileNotSaved = value.isFinalFileEmpty() && !value.isTempFileEmpty()
        val tempFile = if (fileNotSaved && gameId == value.gameId) {
            value.tempFile
        } else {
            if (fileNotSaved && value.gameId.isNotBlank()) {
                stopRecordingFile()
            }
            File(recordsTempDir, DateTime().toString("yyyy-MM-dd-hh-mm-ss") + FormatWAV.FORMAT_NAME)
        }
        recordDisposable = startWriting(tempFile, gameId)
                .subscribeOn(Schedulers.newThread())
                .subscribeWithErrorLogging { relay.accept(it) }
    }

    override fun observeRecordingState(): Flowable<RecordInfo> {
        return relay.distinctUntilChanged().toFlowable(BackpressureStrategy.LATEST)
    }

    private fun startWriting(tempFile: File, gameId: String): Observable<RecordInfo> {
        return Observable.create<RecordInfo> { emitter ->
            val bufferSize = AudioRecord.getMinBufferSize(
                    sampleRate,
                    RawSamples.CHANNEL_CONFIG,
                    RawSamples.AUDIO_FORMAT)
            setThreadPriority()

            var rs: RawSamples? = null
            var recorder: AudioRecord? = null
            var buffer: ShortArray? = null

            try {
                rs = RawSamples(tempFile)
                val samples = rs.samples
                val startTimeMillis = samples / sampleRate * 1000

                rs.open(samples)
                Timber.d(startTimeMillis.toString())

                var min = AudioRecord.getMinBufferSize(sampleRate, RawSamples.CHANNEL_CONFIG, RawSamples.AUDIO_FORMAT)
                if (min <= 0) {
                    val e = IllegalStateException("Unable to initialize AudioRecord: Bad audio values")
                    Timber.e(e)
                    emitter.onNext(RecordInfo(e = e, inProgress = false, gameId = gameId))
                    return@create
                }

                // min = 1 sec
                min = Math.max(sampleRate * if (RawSamples.CHANNEL_CONFIG == AudioFormat.CHANNEL_IN_MONO) 1 else 2, min)

                recorder = AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, RawSamples.CHANNEL_CONFIG, RawSamples.AUDIO_FORMAT, min)

                if (recorder.state != AudioRecord.STATE_INITIALIZED) {
                    val e = IllegalStateException("audio state not initialized")
                    Timber.e(e)
                    emitter.onNext(RecordInfo(e = e, inProgress = false, gameId = gameId))
                    return@create
                }
                val realStart = System.currentTimeMillis() - startTimeMillis
                var tempStart = realStart

                recorder.startRecording()

                var stableRefresh = false
                while (isEndedRecording(emitter)) {
                    if (buffer == null || buffer.size != bufferSize) {
                        buffer = ShortArray(bufferSize)
                    }

                    val readSize = recorder.read(buffer, 0, buffer.size)
                    if (readSize <= 0) {
                        break
                    }
                    val currentTimeMillis = System.currentTimeMillis()

                    val diff = (currentTimeMillis - tempStart) * sampleRate / 1000
                    val totalMillis = currentTimeMillis - realStart
                    tempStart = currentTimeMillis

                    val s = if (RawSamples.CHANNEL_CONFIG == AudioFormat.CHANNEL_IN_MONO) readSize else readSize / 2

                    if (stableRefresh || diff >= s) {
                        stableRefresh = true
                        rs.write(buffer)
                        emitter.onNext(RecordInfo(startTimeMillis, totalMillis, tempFile, inProgress = true, gameId = gameId))
                    }
                }
            } catch (e: Exception) {
                emitter.onNext(relay.value.copy(e = e, inProgress = false, gameId = gameId))
            } finally {
                recorder?.release()
                rs?.close()
                relay.accept(relay.value.copy(inProgress = false, gameId = gameId))
                Timber.d("tempFile exists ${tempFile.exists()}")
            }
        }
    }

    private fun isEndedRecording(emitter: ObservableEmitter<RecordInfo>) =
            !Thread.currentThread().isInterrupted && !emitter.isDisposed

    private fun setThreadPriority() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO)
        val p = Process.getThreadPriority(Process.myTid())

        if (p != Process.THREAD_PRIORITY_URGENT_AUDIO) {
            Timber.e("Unable to set Thread Priority %s", Process.THREAD_PRIORITY_URGENT_AUDIO)
        }
    }

    @Synchronized
    override fun pauseRecordingFile() {
        if (!saveRecordDisposable.isDisposed) {
            return
        }

        recordDisposable.dispose()
        val value = relay.value
        if (value.inProgress) {
            relay.accept(value.copy(inProgress = false))
        }
    }

    @Synchronized
    override fun resumeRecordingFile() {
        if (!recordDisposable.isDisposed) {
            return
        }

        recordDisposable.dispose()
        val value = relay.value
        if (value.gameId.isNotBlank()) {
            startRecordFile(value.gameId)
        }
    }

    @Synchronized
    override fun stopRecordingFile() {
        if (!saveRecordDisposable.isDisposed) {
            return
        }

        recordDisposable.dispose()

        Timber.d("stop")

        saveRecordDisposable = relay.filter { !it.inProgress }
                .firstOrError()
                .flatMap { value ->
                    Single.fromCallable {
                        Timber.d("before encode ${value.tempFile.absolutePath}")
                        if (!value.isTempFileEmpty() && value.gameId.isNotBlank()) {
                            val tempFile = value.tempFile
                            val recordsDir = fileInfoProvider.getRecordsDir(value.gameId)
                            recordsDir.mkdirs()
                            val finalFile = File(recordsDir, tempFile.name)
                            val encoder = FileEncoder(tempFile, FormatWAV(getInfo(), finalFile))

                            try {
                                encoder.encode()
                                tempFile.delete()
                                return@fromCallable (value.copy(finalFile = finalFile, inProgress = false))
                            } catch (e: Exception) {
                                tempFile.delete()
                                return@fromCallable (value.copy(e = e, inProgress = false))
                            }
                        } else {
                            Timber.e("No temp file")
                            return@fromCallable (relay.value.copy(e = IllegalStateException("No temp file"), inProgress = false))
                        }
                    }
                }.subscribeOn(Schedulers.newThread()).subscribeWithErrorLogging {
                    relay.accept(it)
                    relay.accept(RecordInfo())
                }
    }

    private fun getInfo(): EncoderInfo {
        val channels = if (RawSamples.CHANNEL_CONFIG == AudioFormat.CHANNEL_IN_STEREO) 2 else 1
        val bps = if (RawSamples.AUDIO_FORMAT == AudioFormat.ENCODING_PCM_16BIT) 16 else 8

        return EncoderInfo(channels, sampleRate, bps)
    }
}

interface SoundRecordInteractor {
    fun startRecordFile(gameId: String)
    fun stopRecordingFile()
    fun observeRecordingState(): Flowable<RecordInfo>
    fun pauseRecordingFile()
    fun resumeRecordingFile()
}

data class RecordInfo(
        val startTime: Long = 0L,
        val timePassed: Long = 0L,
        val tempFile: File = File(""),
        val finalFile: File = File(""),
        val e: Exception? = null,
        val inProgress: Boolean = false,
        val gameId: String = StringUtils.EMPTY_STRING
) {
    fun isTempFileEmpty(): Boolean {
        return !tempFile.exists()
    }

    fun isFinalFileEmpty(): Boolean {
        return !finalFile.exists()
    }

    fun isRecordingFinished(): Boolean {
        return !isFinalFileEmpty()
    }

}