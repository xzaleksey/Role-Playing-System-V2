package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.backdrop

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.IconViewModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.adapter.GameSettingsStatListViewModel

interface DefaultBackDropView {
    fun clearBackView()

    fun updateStartEndScrollPositions(adapterPosition: Int)

    fun scrollToPosition(position: Int)

    fun chooseIcon(callback: (IconViewModel) -> Unit, items: List<IconViewModel>)



    sealed class UiEvent {

        object CollapseFront : UiEvent()
        object ExpandFront : UiEvent()

        class TitleInput(val text: String) : UiEvent()
        class SubtitleInput(val text: String) : UiEvent()

        class SelectItem(
                val gameSettingsStatListViewModel: GameSettingsStatListViewModel,
                val adapterPosition: Int
        ) : UiEvent()

        class DeleteItem(val gameSettingsViewModel: GameSettingsStatListViewModel) : UiEvent()
    }
}