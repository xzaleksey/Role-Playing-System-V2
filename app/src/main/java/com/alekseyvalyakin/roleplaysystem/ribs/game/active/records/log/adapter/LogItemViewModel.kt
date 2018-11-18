package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.log.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

data class LogItemTextViewModel(
        val id: String,
        val text: String
) : AbstractFlexibleItem<LogTextViewHolder>() {

    override fun createViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>, inflater: LayoutInflater, parent: ViewGroup): LogTextViewHolder {
        val view = LogTextItemView(parent.context)
        view.layoutParams = RecyclerView.LayoutParams(matchParent, wrapContent)
        return LogTextViewHolder(view, adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>?, viewHolder: LogTextViewHolder, position: Int, payloads: MutableList<Any?>?) {
        viewHolder.bind(this)
    }

    override fun getLayoutRes(): Int {
        return FlexibleLayoutTypes.LOG_ITEM_TEXT
    }
}