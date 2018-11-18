package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio.adapter

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio.AudioPresenter
import com.jakewharton.rxrelay2.Relay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

class AudioAdapter(
        items: List<IFlexible<*>>,
        val relay: Relay<AudioPresenter.UiEvent>
) : FlexibleAdapter<IFlexible<*>>(items)