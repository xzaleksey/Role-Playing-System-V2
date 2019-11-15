package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent
import java.io.File

data class AudioItemViewModel(
        val file: File,
        val text: String,
        val secondaryText: String,
        val selected: Boolean,
        val isPlaying: Boolean
) : AbstractFlexibleItem<AudioViewHolder>() {

    override fun createViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>, inflater: LayoutInflater, parent: ViewGroup): AudioViewHolder {
        val view = AudioItemView(parent.context)
        view.layoutParams = RecyclerView.LayoutParams(matchParent, wrapContent)
        return AudioViewHolder(view, adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>, viewHolder: AudioViewHolder, position: Int, payloads: MutableList<Any?>?) {
        viewHolder.bind(this, (adapter as AudioAdapter).relay)
    }

    override fun getLayoutRes(): Int {
        return FlexibleLayoutTypes.AUDIO_ITEM
    }
}