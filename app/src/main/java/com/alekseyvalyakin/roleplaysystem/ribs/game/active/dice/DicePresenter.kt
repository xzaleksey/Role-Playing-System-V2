package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceCollection
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

        class SelectCollection(val diceCollection: DiceCollection) : UiEvent()
        object UnSelectCollection : UiEvent()
        class DeleteCollection(val diceCollection: DiceCollection) : UiEvent()

        object Cancel : UiEvent()
        object Save : UiEvent()
        object Throw : UiEvent()
    }

    fun update(diceViewModel: DiceViewModel)

    fun observeUiEvents(): Observable<UiEvent>
}