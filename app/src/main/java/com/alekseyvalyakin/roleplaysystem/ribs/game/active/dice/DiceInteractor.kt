package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice

import com.alekseyvalyakin.roleplaysystem.data.game.Game
import com.alekseyvalyakin.roleplaysystem.data.game.dice.DicesRepository
import com.alekseyvalyakin.roleplaysystem.data.game.dice.FirebaseDiceCollection
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceCollection
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceCollectionResult
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.SingleDiceCollection
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.viewmodel.DiceViewModelProvider
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.jakewharton.rxrelay2.BehaviorRelay
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import timber.log.Timber
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
    @Inject
    lateinit var game: Game
    @Inject
    lateinit var diceRepository: DicesRepository

    private val relay = BehaviorRelay.createDefault(getEmptyModel())

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
            is DicePresenter.UiEvent.Cancel -> {
                Observable.fromCallable {
                    relay.accept(getEmptyModel())
                }
            }
            is DicePresenter.UiEvent.Save -> {
                return diceRepository.createDocument(
                        gameId = game.id,
                        data = FirebaseDiceCollection.newInstance(DiceCollection.createDiceCollectionFromSingleDiceCollections(
                                relay.value.dices
                        ))
                ).toObservable()
            }
            is DicePresenter.UiEvent.SelectCollection -> {
                Observable.fromCallable {
                    relay.accept(DicesInteractorModel(uiEvent.diceCollection.toSingleDiceCollections()))
                }
            }

            is DicePresenter.UiEvent.UnSelectCollection -> {
                Observable.fromCallable {
                    relay.accept(getEmptyModel())
                }
            }
            is DicePresenter.UiEvent.DeleteCollection -> {
                return diceRepository.deleteDocument(uiEvent.diceCollection.id, game.id).toObservable<Any>()
            }

            is DicePresenter.UiEvent.Throw -> {
                Observable.fromCallable {
                    router.attachDiceResult(DiceCollectionResult.createResult(relay.value.dices))
                }
            }

        }.onErrorReturn {
            Timber.e(it)
        }
    }

    override fun handleBackPress(): Boolean {
        return router.backPress()
    }

    private fun getEmptyModel() = DicesInteractorModel(SingleDiceCollection.createSingleDiceCollectionList())

    class DicesInteractorModel(
            val dices: List<SingleDiceCollection>
    )

}
