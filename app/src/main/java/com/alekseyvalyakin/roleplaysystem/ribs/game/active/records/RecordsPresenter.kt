package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records

import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface RecordsPresenter {

    sealed class UiEvent {
        class SearchInput(val text: String) : UiEvent()
        object OpenTexts : UiEvent()
        object OpenAudio : UiEvent()
        object StopRecording : UiEvent()
        class PauseRecording(val recordState: RecordState) : UiEvent()
        class SaveRecord(val recordState: RecordState, val newFileName: String) : UiEvent()
    }

    fun observeUiEvents(): Observable<UiEvent>

    fun update(viewModel: RecordsViewModel)

    fun clearSearchInput()

    fun updateRecordState(viewModel: RecordState)

    fun showSuccessSave()
}