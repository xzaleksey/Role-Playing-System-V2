package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult

import android.view.LayoutInflater
import android.view.ViewGroup

import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceResult
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceType

import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem

data class SingleDiceTypeResultViewModel(
        val diceResults: List<DiceResult>,
        val totalResultValue: Int,
        val diceType: DiceType) : AbstractFlexibleItem<SingleDiceTypeResultViewHolder>() {

    override fun getLayoutRes(): Int {
        return FlexibleLayoutTypes.SINGLE_DICE_RESULT
    }

    override fun createViewHolder(adapter: FlexibleAdapter<*>, inflater: LayoutInflater, parent: ViewGroup): SingleDiceTypeResultViewHolder {
        return SingleDiceTypeResultViewHolder(SingleDiceTypeResultView(parent.context), adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<*>, holder: SingleDiceTypeResultViewHolder, position: Int, payloads: List<*>?) {
        if (adapter is DiceResultAdapter) {
            holder.bind(this, adapter.relay)
        } else {
            throw IllegalArgumentException("no DiceAdapter")
        }
    }
}

