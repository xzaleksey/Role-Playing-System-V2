package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult

import io.reactivex.Observable

/**
 * Presenter interface implemented by this RIB's view.
 */
interface DiceResultPresenter {

    sealed class UiEvent {
        object Back : UiEvent()
        object Rethrow : UiEvent()
    }

    fun observeUiEvents(): Observable<UiEvent>
    fun update(diceResultViewModel: DiceResultViewModel)
}