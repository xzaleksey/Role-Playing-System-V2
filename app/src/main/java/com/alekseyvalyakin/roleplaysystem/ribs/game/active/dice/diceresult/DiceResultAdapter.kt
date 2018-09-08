package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult

import com.jakewharton.rxrelay2.Relay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

class DiceResultAdapter(
        items: List<IFlexible<*>>,
        val relay: Relay<DiceResultPresenter.UiEvent>
) : FlexibleAdapter<IFlexible<*>>(items)