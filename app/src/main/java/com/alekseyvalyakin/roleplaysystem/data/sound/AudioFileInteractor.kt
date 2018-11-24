package com.alekseyvalyakin.roleplaysystem.data.sound

import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import timber.log.Timber
import java.io.File
import java.io.Serializable
import java.util.concurrent.TimeUnit

class AudioFileInteractorImpl(
        private val exoPlayerInteractor: ExoPlayerInteractor
) : AudioFileInteractor {

    private val relay = BehaviorRelay.createDefault(AudioState())
    private var disposable = Disposables.disposed()
    private val maxProgress = 100

    override fun currentState(): AudioState {
        return relay.value!!
    }

    override fun playFile(file: File): Boolean {
        if (relay.value.file == file) {
            resume()
            return true
        }

        if (file.exists()) {
            exoPlayerInteractor.playFile(file)
            relay.accept(AudioState(file,
                    RawSamples(file).duration,
                    isPlaying = true))
            subscribeUpdates()
            return true
        }

        return false
    }

    override fun resume() {
        if (relay.value.isPlaying) {
            return
        }
        Timber.d("audio: resume")
        subscribeUpdates()
        relay.accept(relay.value.copy(isPlaying = true))
        exoPlayerInteractor.resume()
    }

    override fun pause() {
        if (!relay.value.isPlaying) {
            return
        }
        Timber.d("audio: pause")
        disposable.dispose()
        relay.accept(relay.value.copy(isPlaying = false))
        exoPlayerInteractor.pause()
    }

    override fun stop() {
        if (relay.value.isEmpty()) {
            return
        }
        Timber.d("audio: stop")
        disposable.dispose()
        exoPlayerInteractor.stop()
        relay.accept(AudioState())
    }

    override fun seekTo(progress: Int) {
        if (relay.value.isEmpty()) {
            return
        }
        exoPlayerInteractor.seekTo(progress)
    }


    private fun subscribeUpdates() {
        disposable = Flowable.interval(500L, TimeUnit.MILLISECONDS, Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWithErrorLogging {
                    val currentProgress = exoPlayerInteractor.getProgress()
                    val value = relay.value
                    val ended = exoPlayerInteractor.isStateEnded()
                    if (value.currentProgress < maxProgress && ended) {
                        relay.accept(value.copy(currentProgress = maxProgress))
                        stop()
                    } else if (ended) {
                        stop()
                    } else {
                        relay.accept(value.copy(currentProgress = currentProgress))
                    }
                }
    }

    override fun observe(): Flowable<AudioState> {
        return relay.toFlowable(BackpressureStrategy.LATEST)
    }
}

interface AudioFileInteractor {
    fun observe(): Flowable<AudioState>
    fun playFile(file: File): Boolean
    fun pause()
    fun resume()
    fun stop()
    fun seekTo(progress: Int)
    fun currentState():AudioState
}

data class AudioState(
        val file: File = File(StringUtils.EMPTY_STRING),
        val totalDuration: Long = 0L,
        val currentProgress: Int = 0,
        val isPlaying: Boolean = false
) : Serializable {
    fun isEmpty(): Boolean {
        return !file.exists()
    }

    fun fileNameWithoutExtension(): String {
        return file.nameWithoutExtension
    }

    fun getDurationUntilEnd(): String {
        return DateTime(totalDuration - totalDuration * currentProgress / 100)
                .withZone(DateTimeZone.UTC)
                .toString("HH:mm:ss")
    }

    fun getDurationString(): String {
        return DateTime(totalDuration)
                .withZone(DateTimeZone.UTC)
                .toString("HH:mm:ss")
    }
}