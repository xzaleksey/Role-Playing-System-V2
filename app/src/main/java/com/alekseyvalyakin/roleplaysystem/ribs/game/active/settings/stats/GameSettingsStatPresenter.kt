package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.IconViewModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.adapter.GameSettingsStatListViewModel
import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface GameSettingsStatPresenter {

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

    fun expandFront()

    fun collapseFront()

    fun clearBackView()

    fun updateStartEndScrollPositions(adapterPosition: Int)

    fun scrollToPosition(position: Int)

    fun chooseIcon(callback: (IconViewModel) -> Unit, items: List<IconViewModel>)
}