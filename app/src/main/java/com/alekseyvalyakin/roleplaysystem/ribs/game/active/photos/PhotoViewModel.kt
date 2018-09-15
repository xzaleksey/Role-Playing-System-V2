package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos

import eu.davidea.flexibleadapter.items.IFlexible

data class PhotoViewModel(
        val items: List<IFlexible<*>>,
        val fabState: FabState
)

enum class FabState {
    VISIBLE,
    HIDDEN,
    LOADING
}