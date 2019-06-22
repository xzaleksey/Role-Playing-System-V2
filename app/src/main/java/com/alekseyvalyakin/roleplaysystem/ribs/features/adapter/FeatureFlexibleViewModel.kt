package com.alekseyvalyakin.roleplaysystem.ribs.features.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alekseyvalyakin.roleplaysystem.data.firestore.features.Feature
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

data class FeatureFlexibleViewModel(
        val feature: Feature,
        val canVote: () -> Boolean,
        val votedByMe: Boolean
) : AbstractFlexibleItem<FeatureItemViewHolder>() {

    fun getName(): String {
        return feature.name
    }

    fun getDescription(): String {
        return feature.description
    }

    fun getVotesCount(): Int {
        return feature.votes.size
    }

    override fun createViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>, inflater: LayoutInflater, parent: ViewGroup): FeatureItemViewHolder {
        return FeatureItemViewHolder(FeatureItemView(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(matchParent, wrapContent)
        }, adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<out IFlexible<*>>, holder: FeatureItemViewHolder, position: Int, payloads: MutableList<Any?>) {
        holder.bind(this, (adapter as FeaturesAdapter).relay)
    }
}