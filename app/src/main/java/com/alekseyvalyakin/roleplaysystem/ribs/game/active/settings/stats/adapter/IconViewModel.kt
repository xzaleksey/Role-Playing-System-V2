package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.adapter

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

data class IconViewModel(
        val drawable: Drawable,
        val id: String = "default") : AbstractFlexibleItem<IconViewHolder>() {

    override fun createViewHolder(adapter: FlexibleAdapter<IFlexible<*>>, inflater: LayoutInflater, parent: ViewGroup): IconViewHolder {
        return IconViewHolder(IconView(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(matchParent, wrapContent)
        }, adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>?, holder: IconViewHolder, position: Int, payloads: MutableList<Any?>?) {
        holder.update(this)
    }
}