package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.SingleDiceCollection
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.viewmodel.DiceViewModelProvider
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.jakewharton.rxrelay2.BehaviorRelay
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import javax.inject.Inject

/**
 * Coordinates Business Logic for [DiceScope].
 *
 */
@RibInteractor
class DiceInteractor : BaseInteractor<DicePresenter, DiceRouter>() {

    @Inject
    lateinit var presenter: DicePresenter
    @Inject
    lateinit var diceViewModelProvider: DiceViewModelProvider

    private val relay = BehaviorRelay.createDefault(DicesInteractorModel(
            SingleDiceCollection.createSingleDiceCollectionList())
    )

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        diceViewModelProvider.observeViewModel(relay.toFlowable(BackpressureStrategy.LATEST))
                .subscribeWithErrorLogging {
                    presenter.update(it)
                }

        presenter.observeUiEvents().flatMap {
            handleUiEvent(it)
        }.subscribeWithErrorLogging()
                .addToDisposables()
    }

    private fun handleUiEvent(uiEvent: DicePresenter.UiEvent): Observable<*> {
        return when (uiEvent) {
            is DicePresenter.UiEvent.IncrementSingleDice -> {
                Observable.fromCallable {
                    uiEvent.singleDiceCollection.addDices(1)
                    relay.accept(relay.value)
                }
            }
            is DicePresenter.UiEvent.DecrementSingleDice -> {
                Observable.fromCallable {
                    uiEvent.singleDiceCollection.removeDices(1)
                    relay.accept(relay.value)
                }
            }
        }
    }

    override fun willResignActive() {
        super.willResignActive()

    }

    class DicesInteractorModel(
            val dices: List<SingleDiceCollection>
    )

}
