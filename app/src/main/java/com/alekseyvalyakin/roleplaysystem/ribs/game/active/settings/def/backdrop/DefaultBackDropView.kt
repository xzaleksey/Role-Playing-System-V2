package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.backdrop

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.def.IconViewModel

interface DefaultBackDropView {
    fun updateStartEndScrollPositions(adapterPosition: Int)

    fun scrollToPosition(position: Int)

    fun chooseIcon(callback: (IconViewModel) -> Unit, items: List<IconViewModel>)

    fun expandFront()

    fun collapseFront()
}