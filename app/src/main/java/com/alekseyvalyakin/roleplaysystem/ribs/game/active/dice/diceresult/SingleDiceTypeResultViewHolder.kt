package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.customview.customView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceResult
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceType
import com.alekseyvalyakin.roleplaysystem.utils.getCommonDimen
import com.jakewharton.rxrelay2.Relay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent
import java.util.*

class SingleDiceTypeResultViewHolder(
        private val view: SingleDiceTypeResultView, mAdapter: FlexibleAdapter<*>
) : FlexibleViewHolder(view, mAdapter) {

    init {
        val layoutParams = RecyclerView.LayoutParams(matchParent, wrapContent)
        val commonDimen = view.context.getCommonDimen()
        layoutParams.leftMargin = commonDimen
        layoutParams.rightMargin = commonDimen
        layoutParams.bottomMargin = commonDimen
        view.layoutParams = layoutParams
    }

    @SuppressLint("SetTextI18n")
    fun bind(singleDiceTypeResultViewModel: SingleDiceTypeResultViewModel, relay: Relay<DiceResultPresenter.UiEvent>) {
        view.update(singleDiceTypeResultViewModel, getThrowListener(
                singleDiceTypeResultViewModel.diceResults, relay, singleDiceTypeResultViewModel.diceType
        ))
    }

    private fun getThrowListener(diceResults: List<DiceResult>, relay: Relay<DiceResultPresenter.UiEvent>, diceType: DiceType): View.OnClickListener {

        return View.OnClickListener { _ ->

            val diceMaxResult = "d" + diceType.maxValue

            if (diceResults.size == 1) {
                relay.accept(DiceResultPresenter.UiEvent.RethrowDices(diceResults.toSet()))
                return@OnClickListener
            }
            val singleDiceMaxResult = 1.toString() + diceMaxResult

            val dialogView = DiceResultDialogRethrowView(itemView.context)
            val diceResultViewMap = HashMap<View, DiceResult>()
            val selectedDiceResults = HashSet<DiceResult>()

            val materialDialog = MaterialDialog(itemView.context)
                    .title(R.string.rethrow)
                    .customView(view = dialogView,scrollable = true)
                    .negativeButton(android.R.string.cancel)
                    .positiveButton(R.string.throw_dice, click = {
                        relay.accept(DiceResultPresenter.UiEvent.RethrowDices(selectedDiceResults))
                    })

            val actionButton = materialDialog.getActionButton(WhichButton.POSITIVE)
            actionButton.isEnabled = false

            val diceContainer = dialogView.diceContainer
            val allCheckbox = dialogView.checkBox
            val allContainer = dialogView.allContainer

            for (diceResult in diceResults) {
                val diceView = DiceResultDialogRethrowItemView(itemView.context)
                (diceView.ivMainDice).setImageResource(diceType.resId)
                diceView.tvDiceCount.text = singleDiceMaxResult
                diceView.tvMainResult.text = diceResult.value.toString()
                val checkBox = diceView.diceCheckBox
                diceView.setOnClickListener({ v1 -> getOnClickListener(diceResults, allCheckbox, selectedDiceResults, diceResult, checkBox, actionButton) })
                checkBox.setOnClickListener({ v1 -> getOnClickListener(diceResults, allCheckbox, selectedDiceResults, diceResult, checkBox, actionButton) })
                diceContainer.addView(diceView)
                diceResultViewMap[diceView] = diceResult
            }

            allContainer.setOnClickListener({ v12 -> getAllCLickListener(diceResults, diceResultViewMap, selectedDiceResults, actionButton, allCheckbox) })
            allCheckbox.setOnClickListener({ v12 -> getAllCLickListener(diceResults, diceResultViewMap, selectedDiceResults, actionButton, allCheckbox) })

            materialDialog.show()
        }
    }

    private fun getAllCLickListener(diceResults: List<DiceResult>, diceResultViewMap: Map<View, DiceResult>, selectedDiceResults: MutableSet<DiceResult>, actionButton: Button, allCheckbox: CheckBox) {
        if (selectedDiceResults.size == diceResults.size) {
            selectedDiceResults.clear()
            for (view in diceResultViewMap.keys) {
                (view.findViewById<View>(R.id.checkbox) as CheckBox).isChecked = false
            }
            allCheckbox.isChecked = false
        } else {
            selectedDiceResults.addAll(diceResults)
            for (view in diceResultViewMap.keys) {
                (view.findViewById<View>(R.id.checkbox) as CheckBox).isChecked = true
            }
            allCheckbox.isChecked = true
        }
        actionButton.isEnabled = selectedDiceResults.size > 0
    }

    private fun getOnClickListener(diceResults: List<DiceResult>, allCheckbox: CheckBox, selectedDiceResults: MutableSet<DiceResult>, diceResult: DiceResult, checkBox: CheckBox, actionButton: Button) {
        if (selectedDiceResults.contains(diceResult)) {
            selectedDiceResults.remove(diceResult)
            checkBox.isChecked = false
        } else {
            selectedDiceResults.add(diceResult)
            checkBox.isChecked = true
        }
        allCheckbox.isChecked = selectedDiceResults.size == diceResults.size
        actionButton.isEnabled = selectedDiceResults.size > 0
    }
}
