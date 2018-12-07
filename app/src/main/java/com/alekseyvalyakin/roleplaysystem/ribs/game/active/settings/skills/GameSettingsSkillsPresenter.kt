package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.races.adapter.GameSettingsRaceListViewModel
import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface GameSettingsSkillsPresenter {

    sealed class UiEvent {
        object CollapseFront : UiEvent()
        object ExpandFront : UiEvent()

        class TitleInput(val text: String) : UiEvent()
        class SubtitleInput(val text: String) : UiEvent()
        class SelectSkill(
                val listViewModel: GameSettingsRaceListViewModel,
                val adapterPosition: Int
        ) : UiEvent()


        class ChangeRace(
                val listViewModel: GameSettingsRaceListViewModel
        ) : UiEvent()

        class DeleteSkill(val listViewModel: GameSettingsRaceListViewModel) : UiEvent()
    }

    fun observeUiEvents(): Observable<UiEvent>
    fun updateStartEndScrollPositions(adapterPosition: Int)
    fun scrollToPosition(position: Int)
    fun update(viewModel: GameSettingsSkillViewModel)
    fun expandFront()
    fun collapseFront()
}