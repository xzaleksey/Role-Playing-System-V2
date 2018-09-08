package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult

import android.view.View
import com.jakewharton.rxrelay2.Relay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder

class DiceTotalResultViewHolder(
        private val view: DiceTotalResultView,
        mAdapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, mAdapter) {

    fun bind(diceSingleCollectionViewModel: DiceTotalResultViewModel, relay: Relay<DiceResultPresenter.UiEvent>) {
        view.update(diceSingleCollectionViewModel.currentResultValue, diceSingleCollectionViewModel.maxResultValue,
                View.OnClickListener { relay.accept(DiceResultPresenter.UiEvent.Rethrow) })

    }

}
