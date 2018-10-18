package com.alekseyvalyakin.roleplaysystem.ribs.features.adapter

import com.alekseyvalyakin.roleplaysystem.ribs.features.FeaturesPresenter
import com.jakewharton.rxrelay2.Relay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

class FeaturesAdapter(
        items: List<IFlexible<*>>,
        val relay: Relay<FeaturesPresenter.UiEvent>
) : FlexibleAdapter<IFlexible<*>>(items)