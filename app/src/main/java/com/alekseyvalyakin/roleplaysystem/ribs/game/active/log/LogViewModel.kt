package com.alekseyvalyakin.roleplaysystem.ribs.game.active.log

import eu.davidea.flexibleadapter.items.IFlexible

data class LogViewModel(
        val items: List<IFlexible<*>>,
        val fabState: FabState
)

enum class FabState {
    VISIBLE,
    HIDDEN,
    LOADING
}