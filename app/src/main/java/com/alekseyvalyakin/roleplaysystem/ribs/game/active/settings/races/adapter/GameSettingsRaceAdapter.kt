package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.races.adapter

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.races.GameSettingsRacePresenter
import com.jakewharton.rxrelay2.PublishRelay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

class GameSettingsRaceAdapter(
        val relay: PublishRelay<GameSettingsRacePresenter.UiEvent>
) : FlexibleAdapter<IFlexible<*>>(emptyList())