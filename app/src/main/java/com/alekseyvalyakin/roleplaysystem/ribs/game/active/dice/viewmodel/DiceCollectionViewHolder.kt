package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.viewmodel

import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.DicePresenter
import com.alekseyvalyakin.roleplaysystem.views.DiceCollectionView
import com.jakewharton.rxrelay2.Relay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder

class DiceCollectionViewHolder(
        private val singleDiceView: DiceCollectionView,
        mAdapter: FlexibleAdapter<*>
) : FlexibleViewHolder(singleDiceView, mAdapter) {

    fun bind(diceSingleCollectionViewModel: DiceCollectionViewModel, relay: Relay<DicePresenter.UiEvent>) {
        val clickListener = View.OnClickListener {
            if (diceSingleCollectionViewModel.isSelected) {
                relay.accept(DicePresenter.UiEvent.UnSelectCollection)
            } else {
                relay.accept(DicePresenter.UiEvent.SelectCollection(diceSingleCollectionViewModel.diceCollection))
            }
        }

        val longClickListener = View.OnLongClickListener { _ ->
            MaterialDialog(singleDiceView.context)
                    .title(text = singleDiceView.context.getString(R.string.delete) + "?")
                    .message(R.string.dice_collections_delete_message)
                    .negativeButton(android.R.string.cancel)
                    .positiveButton(android.R.string.ok, click = {
                        relay.accept(DicePresenter.UiEvent.DeleteCollection(diceSingleCollectionViewModel.diceCollection))
                    })
                    .show()
            true
        }

        singleDiceView.update(diceSingleCollectionViewModel, clickListener, longClickListener)
    }

}
