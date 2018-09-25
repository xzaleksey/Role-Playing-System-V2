package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes.adapter

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes.GameSettingsClassPresenter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.GameSettingsStatPresenter
import com.jakewharton.rxrelay2.PublishRelay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

class GameSettingsClassAdapter(
        val relay: PublishRelay<GameSettingsClassPresenter.UiEvent>
) : FlexibleAdapter<IFlexible<*>>(emptyList())