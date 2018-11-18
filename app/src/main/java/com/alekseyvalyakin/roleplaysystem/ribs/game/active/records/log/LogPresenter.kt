package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.log


import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.RecordState
import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface LogPresenter {

    sealed class UiEvent {
        class SendTextMessage(val text: String) : UiEvent()
        object StartRecording : UiEvent()
    }

    fun observeUiEvents(): Observable<UiEvent>

    fun update(viewModel: LogViewModel)

    fun updateRecordState(recordState: RecordState)
}