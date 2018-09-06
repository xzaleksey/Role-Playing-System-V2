package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.adapter

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.DicePresenter
import com.jakewharton.rxrelay2.Relay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

class DiceAdapter(
        items: List<IFlexible<*>>,
        val relay: Relay<DicePresenter.UiEvent>
) : FlexibleAdapter<IFlexible<*>>(items)