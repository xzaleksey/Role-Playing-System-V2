package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

data class AudioItemViewModel(
        val id: String,
        val text: String
) : AbstractFlexibleItem<AudioViewHolder>() {

    override fun createViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>, inflater: LayoutInflater, parent: ViewGroup): AudioViewHolder {
        val view = AudioItemView(parent.context)
        view.layoutParams = RecyclerView.LayoutParams(matchParent, wrapContent)
        return AudioViewHolder(view, adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>, viewHolder: AudioViewHolder, position: Int, payloads: MutableList<Any?>?) {
        viewHolder.bind(this, adapter as AudioAdapter)
    }

    override fun getLayoutRes(): Int {
        return FlexibleLayoutTypes.AUDIO_ITEM
    }
}