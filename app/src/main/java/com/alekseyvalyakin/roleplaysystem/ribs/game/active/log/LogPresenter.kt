package com.alekseyvalyakin.roleplaysystem.ribs.game.active.log

import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface LogPresenter {

    sealed class UiEvent {
        class SendTextMessage(val text: String) : UiEvent()
        class SearchInput(val text: String) : UiEvent()
        object OpenTexts : UiEvent()
        object OpenAudio : UiEvent()
        object StartRecording : UiEvent()
        object StopRecording : UiEvent()
        class PauseRecording(val logRecordState: LogRecordState) : UiEvent()
        class SaveRecord(val logRecordState: LogRecordState, val newFileName: String) : UiEvent()
    }

    fun observeUiEvents(): Observable<UiEvent>

    fun update(viewModel: LogViewModel)

    fun clearSearchInput()

    fun updateRecordState(viewModel: LogRecordState)
}