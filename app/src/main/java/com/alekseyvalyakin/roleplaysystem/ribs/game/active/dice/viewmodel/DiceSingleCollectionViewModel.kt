package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes.SINGLE_DICE_COLLECTION
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.adapter.DiceAdapter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.SingleDiceCollection
import com.alekseyvalyakin.roleplaysystem.views.SingleDiceView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem

data class DiceSingleCollectionViewModel(
        val mainDiceResId: Int,
        val diceCollection: SingleDiceCollection
) : AbstractFlexibleItem<DiceSingleCollectionViewHolder>() {

    override fun getLayoutRes(): Int {
        return SINGLE_DICE_COLLECTION
    }

    override fun createViewHolder(adapter: FlexibleAdapter<*>, inflater: LayoutInflater, parent: ViewGroup): DiceSingleCollectionViewHolder {
        return DiceSingleCollectionViewHolder(SingleDiceView(parent.context), adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<*>, holder: DiceSingleCollectionViewHolder, position: Int, payloads: List<*>) {
        holder.bind(this, (adapter as DiceAdapter).relay)
    }
}
