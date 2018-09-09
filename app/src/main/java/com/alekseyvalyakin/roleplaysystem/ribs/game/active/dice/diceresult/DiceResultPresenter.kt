package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceResult
import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface DiceResultPresenter {

    sealed class UiEvent {
        object Back : UiEvent()
        object RethrowAllDices : UiEvent()
        class RethrowDices(val diceResults: Set<DiceResult>) : UiEvent()
    }

    fun observeUiEvents(): Observable<UiEvent>
    fun update(diceResultViewModel: DiceResultViewModel)
}