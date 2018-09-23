package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings

import eu.davidea.flexibleadapter.items.IFlexible

data class GameSettingsViewModel(
        val items: List<IFlexible<*>>
) {

    enum class GameSettingsItemType {
        STATS,
        CLASSES,
        RACES,
        SKILLS,
        SPELLS,
        EQUIPMENT,
        DICES,
    }
}