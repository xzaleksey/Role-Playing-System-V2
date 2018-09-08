package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes
import com.alekseyvalyakin.roleplaysystem.utils.getCommonDimen
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import org.jetbrains.anko.matchParent

data class DiceTotalResultViewModel(val currentResultValue: String, val maxResultValue: String) : AbstractFlexibleItem<DiceTotalResultViewHolder>() {

    override fun getLayoutRes(): Int {
        return FlexibleLayoutTypes.TOTAL_DICE_RESULT
    }

    override fun createViewHolder(adapter: FlexibleAdapter<*>, inflater: LayoutInflater, parent: ViewGroup): DiceTotalResultViewHolder {
        val view = DiceTotalResultView(parent.context)
        val layoutParams = RecyclerView.LayoutParams(matchParent, view.context.getIntDimen(R.dimen.dp_184))
        layoutParams.leftMargin = view.context.getCommonDimen()
        layoutParams.rightMargin = view.context.getCommonDimen()
        layoutParams.topMargin = view.context.getCommonDimen()
        view.layoutParams = layoutParams
        return DiceTotalResultViewHolder(view, adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<*>, holder: DiceTotalResultViewHolder, position: Int, payloads: List<*>?) {
        if (adapter is DiceResultAdapter) {
            holder.bind(this, adapter.relay)
        } else {
            throw IllegalArgumentException("no DiceAdapter")
        }
    }

}

