package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceCollection
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.SingleDiceCollection
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.viewmodel.DiceViewModelMapper
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
    @Inject
    lateinit var diceViewModelMapper: DiceViewModelMapper

    private val relay = BehaviorRelay.createDefault(DicesInteractorModel(
            SingleDiceCollection.createSingleDiceCollectionList(), emptyList())
    )

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        relay
                .map { diceViewModelMapper.mapDiceViewModel(it) }
                .subscribeWithErrorLogging {
                    presenter.update(it)
                }.addToDisposables()

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
            val dices: List<SingleDiceCollection>,
            val diceCollections: List<DiceCollection>,
            val diceCollectionsLoaded: Boolean = false)

}
