package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes.adapter.GameSettingsClassListViewModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.backdrop.DefaultBackDropView
import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface GameSettingsClassPresenter : DefaultBackDropView {

    sealed class UiEvent {

        object CollapseFront : UiEvent()
        object ExpandFront : UiEvent()

        class TitleInput(val text: String) : UiEvent()
        class SubtitleInput(val text: String) : UiEvent()

        class SelectClass(
                val gameSettingsClassListViewModel: GameSettingsClassListViewModel,
                val adapterPosition: Int
        ) : UiEvent()


        class ChangeClass(
                val gameSettingsListViewModel: GameSettingsClassListViewModel
        ) : UiEvent()

        class DeleteClass(val gameSettingsListViewModel: GameSettingsClassListViewModel) : UiEvent()
    }

    fun observeUiEvents(): Observable<UiEvent>

    fun update(viewModel: GameSettingsClassViewModel)

}