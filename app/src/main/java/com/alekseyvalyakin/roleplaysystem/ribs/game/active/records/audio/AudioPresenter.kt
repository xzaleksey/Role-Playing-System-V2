package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio


/**
 * Presenter interface implemented by this RIB's view.
 */
interface AudioPresenter {
    fun update(viewModel: AudioViewModel)

    sealed class UiEvent {
    }
}