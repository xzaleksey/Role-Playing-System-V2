package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.viewmodel

import eu.davidea.flexibleadapter.items.IFlexible

data class DiceViewModel(
        val diceCollectionsItems: List<IFlexible<*>> = emptyList(),
        val diceItemsCollectionsLoaded:Boolean=false,
        val diceItems: List<IFlexible<*>> = emptyList(),
        val buttonThrowEnabled: Boolean = false,
        val buttonSaveEnabled: Boolean = false,
        val buttonCancelEnabled: Boolean = false
)