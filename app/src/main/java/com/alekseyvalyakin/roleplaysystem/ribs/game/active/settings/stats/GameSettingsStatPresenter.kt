package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats

import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface GameSettingsStatPresenter {

    sealed class UiEvent {
        object BackNavigate : UiEvent()
    }

    fun observeUiEvents(): Observable<UiEvent>
}