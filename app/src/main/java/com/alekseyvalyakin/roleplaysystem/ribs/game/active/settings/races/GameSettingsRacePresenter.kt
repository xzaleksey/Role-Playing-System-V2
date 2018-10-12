package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.races

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.backdrop.DefaultBackDropView
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.races.adapter.GameSettingsRaceListViewModel
import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface GameSettingsRacePresenter : DefaultBackDropView {

    sealed class UiEvent {

        object CollapseFront : UiEvent()
        object ExpandFront : UiEvent()

        class TitleInput(val text: String) : UiEvent()
        class SubtitleInput(val text: String) : UiEvent()

        class SelectRace(
                val listViewModel: GameSettingsRaceListViewModel,
                val adapterPosition: Int
        ) : UiEvent()


        class ChangeRace(
                val listViewModel: GameSettingsRaceListViewModel
        ) : UiEvent()

        class DeleteRace(val listViewModel: GameSettingsRaceListViewModel) : UiEvent()
    }

    fun observeUiEvents(): Observable<UiEvent>

    fun update(viewModel: GameSettingsRaceViewModel)

}