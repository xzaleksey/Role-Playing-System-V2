package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceType
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.viewmodel.DiceSingleCollectionViewModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.viewmodel.DiceViewModel
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.jakewharton.rxrelay2.BehaviorRelay
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Coordinates Business Logic for [DiceScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class DiceInteractor : BaseInteractor<DicePresenter, DiceRouter>() {

    @Inject
    lateinit var presenter: DicePresenter

    private val relay = BehaviorRelay.createDefault(DiceViewModel(diceItems = listOf(
            DiceSingleCollectionViewModel(DiceType.D4.resId, DiceType.D4.createSingleDiceCollection()),
            DiceSingleCollectionViewModel(DiceType.D6.resId, DiceType.D6.createSingleDiceCollection()),
            DiceSingleCollectionViewModel(DiceType.D8.resId, DiceType.D8.createSingleDiceCollection()),
            DiceSingleCollectionViewModel(DiceType.D10.resId, DiceType.D10.createSingleDiceCollection()),
            DiceSingleCollectionViewModel(DiceType.D12.resId, DiceType.D12.createSingleDiceCollection()),
            DiceSingleCollectionViewModel(DiceType.D20.resId, DiceType.D20.createSingleDiceCollection())
    )))

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        relay.subscribeWithErrorLogging {
            presenter.update(it)
        }.addToDisposables()

        presenter.observeUiEvents().flatMap {
            handleUiEvent(it)
        }.subscribeWithErrorLogging()
                .addToDisposables()
    }

    fun handleUiEvent(uiEvent: DicePresenter.UiEvent): Observable<*> {
        return when (uiEvent) {
            is DicePresenter.UiEvent.IncrementSingleDice -> {
                Observable.fromCallable {
                    uiEvent.singleDiceCollection.addDices(1)
                }
            }
            is DicePresenter.UiEvent.DecrementSingleDice -> {
                Observable.fromCallable {
                    uiEvent.singleDiceCollection.removeDices(1)
                }
            }
        }
    }

    override fun willResignActive() {
        super.willResignActive()

    }

}
