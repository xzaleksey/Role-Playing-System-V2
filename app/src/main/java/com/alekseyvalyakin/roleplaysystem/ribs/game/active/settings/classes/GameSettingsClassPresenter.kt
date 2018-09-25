package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes.adapter.GameSettingsClassItemViewModel

/**
 * Presenter interface implemented by this RIB's view.
 */
interface GameSettingsClassPresenter {

    sealed class UiEvent {
        class SelectClass(viewModel: GameSettingsClassItemViewModel, adapterPosition: Int) : UiEvent()
    }
}