package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.adapter

import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

class IconViewHolder(
        private val iconView: IconView,
        adapter: FlexibleAdapter<IFlexible<*>>
) : FlexibleViewHolder(iconView, adapter) {

    fun update(iconViewModel: IconViewModel) {
        iconView.update(iconViewModel)
    }
}