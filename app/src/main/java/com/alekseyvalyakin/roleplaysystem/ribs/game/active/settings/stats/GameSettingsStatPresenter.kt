package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats

import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface GameSettingsStatPresenter {

    sealed class UiEvent {
        object ToolbarLeftClick : UiEvent()
        object ToolbarRightClick : UiEvent()

        object CollapseFront : UiEvent()
        object ExpandFront : UiEvent()

        class TitleInput(val text: String) : UiEvent()

        class SubtitleInput(val text: String) : UiEvent()
    }

    fun observeUiEvents(): Observable<UiEvent>

    fun update(viewModel: GameSettingsStatViewModel)

    fun expandFront()

    fun collapseFront()
}