package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills.adapter

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills.GameSettingsSkillsPresenter
import com.jakewharton.rxrelay2.PublishRelay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

class GameSettingsSkillsAdapter(
        val relay: PublishRelay<GameSettingsSkillsPresenter.UiEvent>
) : FlexibleAdapter<IFlexible<*>>(emptyList())