package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.SingleDiceCollection
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.viewmodel.DiceViewModel
import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface DicePresenter {
    sealed class UiEvent {
        class IncrementSingleDice(val singleDiceCollection: SingleDiceCollection) : UiEvent()

        class DecrementSingleDice(val singleDiceCollection: SingleDiceCollection) : UiEvent()
    }

    fun update(diceViewModel: DiceViewModel)
    fun observeUiEvents(): Observable<UiEvent>
}