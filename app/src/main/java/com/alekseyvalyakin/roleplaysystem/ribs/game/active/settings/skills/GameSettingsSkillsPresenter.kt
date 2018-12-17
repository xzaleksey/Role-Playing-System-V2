package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills.UserGameSkill
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
        class TagAdd(val text: String, val userGameSkill: UserGameSkill) : UiEvent()
        class TagRemove(val text: String, val userGameSkill: UserGameSkill) : UiEvent()
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