package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.backdrop.DefaultBackDropView
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.adapter.GameSettingsStatListViewModel
import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface GameSettingsStatPresenter : DefaultBackDropView {

    sealed class UiEvent {

        object CollapseFront : UiEvent()
        object ExpandFront : UiEvent()

        class TitleInput(val text: String) : UiEvent()
        class SelectStat(
                val gameSettingsStatListViewModel: GameSettingsStatListViewModel,
                val adapterPosition: Int
        ) : UiEvent()

        class ChangeStat(
                val gameSettingsStatListViewModel: GameSettingsStatListViewModel
        ) : UiEvent()


        class SubtitleInput(val text: String) : UiEvent()
        class DeleteStat(val gameSettingsViewModel: GameSettingsStatListViewModel) : UiEvent()
    }

    fun observeUiEvents(): Observable<UiEvent>

    fun update(viewModel: GameSettingsStatViewModel)
}