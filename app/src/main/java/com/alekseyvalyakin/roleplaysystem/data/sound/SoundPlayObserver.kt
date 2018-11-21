package com.alekseyvalyakin.roleplaysystem.data.sound

import android.content.Context
import com.alekseyvalyakin.roleplaysystem.base.observer.DisposableObserver
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function

class SoundPlayObserver(
        private val audioFileInteractor: AudioFileInteractor,
        private val context: Context
) : DisposableObserver {
    override fun subscribe(): Disposable {
        return audioFileInteractor.observe()
                .distinctUntilChanged(Function<AudioState, Boolean> {
                    return@Function it.isPlaying
                })
                .subscribeWithErrorLogging {
                    if (it.isPlaying) {
                        SoundPlayService.startService(context)
                    }
                }
    }
}