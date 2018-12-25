package com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu.adapter

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu.MenuPresenter
import com.jakewharton.rxrelay2.Relay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

class GameMenuAdapter(
        items: List<IFlexible<*>>,
        val relay: Relay<MenuPresenter.UiEvent>
) : FlexibleAdapter<IFlexible<*>>(items)