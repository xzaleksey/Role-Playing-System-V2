package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio

import io.reactivex.Observable
import java.io.File


/**
 * Presenter interface implemented by this RIB's view.
 */
interface AudioPresenter {
    fun update(viewModel: AudioViewModel)

    sealed class UiEvent {
        class TogglePlay(val file: File, val isPlaying: Boolean) : UiEvent()
    }

    fun observe(): Observable<UiEvent>
}