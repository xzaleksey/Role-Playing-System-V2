package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes.DICE_COLLECTION
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.adapter.DiceAdapter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceCollection
import com.alekseyvalyakin.roleplaysystem.views.DiceCollectionView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem

data class DiceCollectionViewModel(
        val diceCollection: DiceCollection,
        val isSelected: Boolean
) : AbstractFlexibleItem<DiceCollectionViewHolder>() {

    override fun getLayoutRes(): Int {
        return DICE_COLLECTION
    }

    override fun createViewHolder(adapter: FlexibleAdapter<*>, inflater: LayoutInflater, parent: ViewGroup): DiceCollectionViewHolder {
        return DiceCollectionViewHolder(DiceCollectionView(parent.context), adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<*>, holder: DiceCollectionViewHolder, position: Int, payloads: List<*>) {
        if (adapter is DiceAdapter) {
            holder.bind(this, adapter.relay)
        } else {
            throw IllegalArgumentException("adapter should be diceAdapter")
        }
    }


}