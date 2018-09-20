package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.adapter

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.GameSettingsStatPresenter
import com.jakewharton.rxrelay2.PublishRelay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

class GameSettingsStatAdapter(
        val relay: PublishRelay<GameSettingsStatPresenter.UiEvent>
) : FlexibleAdapter<IFlexible<*>>(emptyList())