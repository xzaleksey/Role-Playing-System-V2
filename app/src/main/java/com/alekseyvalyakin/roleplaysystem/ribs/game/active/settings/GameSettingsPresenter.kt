package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.adapter.GameSettingsListViewModel
import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface GameSettingsPresenter {
    fun update(gameSettingsViewModel: GameSettingsViewModel)

    sealed class UiEvent {
        class GameSettingsClick(val gameSettingsListViewModel: GameSettingsListViewModel) : UiEvent()
    }

    fun observeUiEvents(): Observable<UiEvent>

}