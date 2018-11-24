package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records

import com.alekseyvalyakin.roleplaysystem.data.sound.AudioState
import io.reactivex.Observable
import java.io.File

/**
 * Presenter interface implemented by this RIB's view.
 */
interface RecordsPresenter {

    sealed class UiEvent {
        class SearchInput(val text: String) : UiEvent()
        object OpenLogs : UiEvent()
        object OpenAudio : UiEvent()
        object StopRecording : UiEvent()
        class PauseRecording(val recordState: RecordState) : UiEvent()
        class SaveRecord(val recordState: RecordState, val newFileName: String) : UiEvent()
        class DeleteFile(val audioState: AudioState) : UiEvent()
        class TogglePlay(val file: File, val isPlaying: Boolean) : UiEvent()
        class SeekTo(val file: File, val progress: Int) : UiEvent()
    }

    fun observeUiEvents(): Observable<UiEvent>

    fun update(viewModel: RecordsViewModel)

    fun clearSearchInput()

    fun updateRecordState(viewModel: RecordState)

    fun showSuccessSave()
    fun updateAudioState(audioState: AudioState)
}