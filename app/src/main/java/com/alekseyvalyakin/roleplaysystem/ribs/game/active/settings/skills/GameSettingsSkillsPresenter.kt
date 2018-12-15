package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills.adapter.GameSettingsSkillsListViewModel
import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface GameSettingsSkillsPresenter {

    sealed class UiEvent {
        object CollapseFront : UiEvent()
        object ExpandFront : UiEvent()

        object AddSuccessCheck : UiEvent()
        object AddResultCheck : UiEvent()
        class TagInput(val text: String) : UiEvent()
        class TitleInput(val text: String) : UiEvent()
        class SubtitleInput(val text: String) : UiEvent()
        class SelectSkill(
                val listViewModel: GameSettingsSkillsListViewModel,
                val adapterPosition: Int
        ) : UiEvent()


        class ChangeSkill(
                val listViewModel: GameSettingsSkillsListViewModel
        ) : UiEvent()

        class DeleteSkill(val listViewModel: GameSettingsSkillsListViewModel) : UiEvent()
    }

    fun observeUiEvents(): Observable<UiEvent>
    fun updateStartEndScrollPositions(adapterPosition: Int)
    fun scrollToPosition(position: Int)
    fun update(viewModel: GameSettingsSkillViewModel)
    fun expandFront()
    fun collapseFront()
}