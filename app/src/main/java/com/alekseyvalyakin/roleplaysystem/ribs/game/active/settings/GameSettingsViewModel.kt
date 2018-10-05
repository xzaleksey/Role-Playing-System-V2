package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings

import eu.davidea.flexibleadapter.items.IFlexible

data class GameSettingsViewModel(
        val items: List<IFlexible<*>>
) {

    enum class GameSettingsItemType(val textId: String) {
        STATS("Stats"),
        CLASSES("Classes"),
        RACES("Races"),
        SKILLS("Skills"),
        SPELLS("Spells"),
        EQUIPMENT("Equipment"),
        DICES("Dices"),
    }
}