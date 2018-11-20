package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio

import com.alekseyvalyakin.roleplaysystem.data.sound.AudioState
import io.reactivex.Observable
import java.io.File


/**
 * Presenter interface implemented by this RIB's view.
 */
interface AudioPresenter {
    fun update(viewModel: AudioViewModel)

    sealed class UiEvent {
        class TogglePlay(val file: File, val isPlaying: Boolean) : UiEvent()
        class DeleteFile(val audioState: AudioState) : UiEvent()
        class SeekTo(val file: File, val progress: Int) : UiEvent()
    }

    fun observe(): Observable<UiEvent>
}