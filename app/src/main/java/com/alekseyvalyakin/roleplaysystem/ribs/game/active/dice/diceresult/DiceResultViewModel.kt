package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult

import eu.davidea.flexibleadapter.items.IFlexible

data class DiceResultViewModel(
        val result: String,
        val maxResult: String,
        val items: List<IFlexible<*>> = emptyList()
)