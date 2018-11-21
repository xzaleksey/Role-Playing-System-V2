package com.alekseyvalyakin.roleplaysystem.data.sound

import android.content.Context
import com.alekseyvalyakin.roleplaysystem.base.observer.DisposableObserver
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function

class RecordSoundObserver(
        private val soundRecordInteractor: SoundRecordInteractor,
        private val context: Context
) : DisposableObserver {
    override fun subscribe(): Disposable {
        return soundRecordInteractor.observeRecordingState()
                .distinctUntilChanged(Function<RecordInfo, Boolean> {
                    return@Function it.inProgress
                })
                .subscribeWithErrorLogging {
                    if (it.inProgress) {
                        SoundRecordService.startService(context)
                    }
                }
    }
}