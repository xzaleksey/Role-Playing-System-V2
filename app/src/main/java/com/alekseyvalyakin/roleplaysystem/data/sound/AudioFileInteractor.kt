package com.alekseyvalyakin.roleplaysystem.data.sound

import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit

class AudioFileInteractorImpl(
        private val exoPlayerInteractor: ExoPlayerInteractor
) : AudioFileInteractor {

    private val relay = BehaviorRelay.createDefault(AudioState())
    private var disposable = Disposables.disposed()
    private val maxProgress = 100L

    override fun playFile(file: File): Boolean {
        if (relay.value.file == file) {
            resume()
            return true
        }

        if (file.exists()) {
            exoPlayerInteractor.playFile(file)
            relay.accept(AudioState(file, isPlaying = true))
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
}

data class AudioState(
        val file: File = File(StringUtils.EMPTY_STRING),
        val currentProgress: Long = 0L,
        val isPlaying: Boolean = false
) {
    fun isEmpty(): Boolean {
        return !file.exists()
    }
}