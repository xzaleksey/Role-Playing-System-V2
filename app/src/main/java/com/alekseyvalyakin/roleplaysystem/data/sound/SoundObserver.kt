package com.alekseyvalyakin.roleplaysystem.data.sound

import com.alekseyvalyakin.roleplaysystem.base.observer.DisposableObserver
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.Flowables

class SoundObserver(
        private val audioFileInteractor: AudioFileInteractor,
        private val soundRecordInteractor: SoundRecordInteractor
) : DisposableObserver {
    override fun subscribe(): Disposable {

        return Flowables.combineLatest(audioFileInteractor.observe(), soundRecordInteractor.observeRecordingState())
                .subscribeWithErrorLogging {
                    val audioState = it.first
                    val recordInfo = it.second
                    if (audioState.isPlaying) {
                        soundRecordInteractor.pauseRecordingFile()
                    } else if (recordInfo.inProgress) {
                        audioFileInteractor.pause()
                    }
                }
    }
}