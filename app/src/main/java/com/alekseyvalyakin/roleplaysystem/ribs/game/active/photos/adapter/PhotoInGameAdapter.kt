package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.adapter

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.PhotoPresenter
import com.jakewharton.rxrelay2.Relay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

class PhotoInGameAdapter(
        items: List<IFlexible<*>>,
        val relay: Relay<PhotoPresenter.UiEvent>
) : FlexibleAdapter<IFlexible<*>>(items)