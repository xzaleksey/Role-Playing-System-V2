package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.adapter

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.GameSettingsPresenter
import com.jakewharton.rxrelay2.Relay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

class GameSettingsAdapter(
        items: List<IFlexible<*>>,
        val relay: Relay<GameSettingsPresenter.UiEvent>
) : FlexibleAdapter<IFlexible<*>>(items) {

}