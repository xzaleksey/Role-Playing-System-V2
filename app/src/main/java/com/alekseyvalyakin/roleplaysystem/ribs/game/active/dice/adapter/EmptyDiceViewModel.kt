package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes.EMPTY_DICE
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem

class EmptyDiceViewModel : AbstractFlexibleItem<EmptyDiceViewHolder>() {

    override fun getLayoutRes(): Int {
        return EMPTY_DICE
    }

    override fun createViewHolder(adapter: FlexibleAdapter<*>, inflater: LayoutInflater, parent: ViewGroup): EmptyDiceViewHolder {
        return EmptyDiceViewHolder(EmptyDiceView(parent.context), adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<*>, holderEmpty: EmptyDiceViewHolder, position: Int, payloads: List<*>) {
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass == other?.javaClass) return true
        return false
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

}
