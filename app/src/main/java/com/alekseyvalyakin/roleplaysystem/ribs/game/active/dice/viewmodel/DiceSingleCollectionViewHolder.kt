package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.viewmodel

import android.view.View
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.DicePresenter
import com.alekseyvalyakin.roleplaysystem.views.SingleDiceView
import com.jakewharton.rxrelay2.Relay

import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder

class DiceSingleCollectionViewHolder(
        private val singleDiceView: SingleDiceView,
        mAdapter: FlexibleAdapter<*>
) : FlexibleViewHolder(singleDiceView, mAdapter) {

    fun bind(diceSingleCollectionViewModel: DiceSingleCollectionViewModel, relay: Relay<DicePresenter.UiEvent>) {
        val incrementClickListener = View.OnClickListener {
            relay.accept(DicePresenter.UiEvent.IncrementSingleDice(diceSingleCollectionViewModel.diceCollection))
        }

        val decrementClickListener = View.OnClickListener {
            relay.accept(DicePresenter.UiEvent.DecrementSingleDice(diceSingleCollectionViewModel.diceCollection))
        }

        singleDiceView.updateView(diceSingleCollectionViewModel, incrementClickListener, incrementClickListener, decrementClickListener)
    }

}
