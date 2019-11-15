package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.dice.DicesRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.dice.FirestoreDiceCollection
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.DiceInteractor.DicesInteractorModel.Companion.KEY
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceCollection
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceCollectionResult
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.SingleDiceCollection
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.viewmodel.DiceViewModelProvider
import com.alekseyvalyakin.roleplaysystem.utils.getNonNullValue
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.jakewharton.rxrelay2.BehaviorRelay
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import com.uber.rib.core.getSerializable
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import timber.log.Timber
import java.io.Serializable
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
    @Inject
    lateinit var analyticsReporter: AnalyticsReporter

    private val relay = BehaviorRelay.createDefault(getEmptyModel())
    private val screenName = "GameDices"

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        analyticsReporter.setCurrentScreen(screenName)

        savedInstanceState?.run {
            relay.accept(this.getSerializable(KEY))
        }
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
                    relay.accept(dicesInteractorModel())
                }
            }
            is DicePresenter.UiEvent.DecrementSingleDice -> {
                Observable.fromCallable {
                    uiEvent.singleDiceCollection.removeDices(1)
                    relay.accept(dicesInteractorModel())
                }
            }
            is DicePresenter.UiEvent.Cancel -> {
                Observable.fromCallable {
                    analyticsReporter.logEvent(GameDiceAnalyticsEvent.CancelButtonCLick(game))
                    relay.accept(getEmptyModel())
                }
            }
            is DicePresenter.UiEvent.Save -> {
                analyticsReporter.logEvent(GameDiceAnalyticsEvent.CreateDiceCollection(game))
                val diceCollection = DiceCollection.createDiceCollectionFromSingleDiceCollections(
                        dicesInteractorModel().dices
                )
                val firebaseDiceCollection = FirestoreDiceCollection.newInstance(diceCollection)
                return diceRepository.createDocument(
                        gameId = game.id,
                        data = firebaseDiceCollection
                ).onErrorReturn {
                    firebaseDiceCollection
                }.toObservable()
            }
            is DicePresenter.UiEvent.SelectCollection -> {
                Observable.fromCallable {
                    analyticsReporter.logEvent(GameDiceAnalyticsEvent.SelectDiceCollection(game))
                    relay.accept(DicesInteractorModel(uiEvent.diceCollection.toSingleDiceCollections()))
                }
            }

            is DicePresenter.UiEvent.UnSelectCollection -> {
                Observable.fromCallable {
                    analyticsReporter.logEvent(GameDiceAnalyticsEvent.UnselectDiceCollection(game))
                    relay.accept(getEmptyModel())
                }
            }
            is DicePresenter.UiEvent.DeleteCollection -> {
                analyticsReporter.logEvent(GameDiceAnalyticsEvent.DeleteGameCollection(game))
                return diceRepository.deleteDocument(uiEvent.diceCollection.id, game.id).toObservable<Any>()
            }

            is DicePresenter.UiEvent.Throw -> {
                Observable.fromCallable {
                    analyticsReporter.logEvent(GameDiceAnalyticsEvent.ThrowDice(game))
                    router.attachDiceResult(DiceCollectionResult.createResult(dicesInteractorModel().dices))
                }
            }

        }.onErrorReturn {
            Timber.e(it)
        }
    }

    private fun dicesInteractorModel() = relay.getNonNullValue()

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY, relay.value)
    }

    override fun handleBackPress(): Boolean {
        return router.backPress()
    }

    private fun getEmptyModel() = DicesInteractorModel(SingleDiceCollection.createSingleDiceCollectionList())

    class DicesInteractorModel(
            val dices: List<SingleDiceCollection>
    ) : Serializable {
        companion object {
            const val KEY = "ModelKey"
        }
    }

}
