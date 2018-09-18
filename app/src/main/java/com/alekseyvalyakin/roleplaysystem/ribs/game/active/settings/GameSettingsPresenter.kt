package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.adapter.GameSettingsListViewModel

/**
 * Presenter interface implemented by this RIB's view.
 */
interface GameSettingsPresenter {
    fun update(gameSettingsViewModel: GameSettingsViewModel)

    sealed class UiEvent {
        class GameSettingsClick(gameSettingsListViewModel: GameSettingsListViewModel) : UiEvent()
        object GameSettingsSkip : UiEvent()
    }
}