package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult

import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityListener
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceCollectionResult
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

/**
 * Coordinates Business Logic for [DiceResultScope].
 *
 */
@RibInteractor
class DiceResultInteractor : BaseInteractor<DiceResultPresenter, DiceResultRouter>() {

    @Inject
    lateinit var presenter: DiceResultPresenter
    @Inject
    lateinit var activityListener: ActivityListener
    @Inject
    lateinit var diceCollectionResult: DiceCollectionResult
    @Inject
    lateinit var stringRepository: StringRepository
    @Inject
    lateinit var diceResultViewModelProvider: DiceResultViewModelProvider

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        presenter.observeUiEvents()
                .subscribeWithErrorLogging { uiEvent ->
                    when (uiEvent) {
                        is DiceResultPresenter.UiEvent.Back -> {
                            activityListener.backPress()
                        }
                        is DiceResultPresenter.UiEvent.RethrowAllDices -> {
                            diceCollectionResult.rethrow()
                            updateView(diceCollectionResult)
                        }

                        is DiceResultPresenter.UiEvent.RethrowDices -> {
                            uiEvent.diceResults.forEach { it.rethrow() }
                            updateView(diceCollectionResult)
                        }

                    }
                }
                .addToDisposables()

        updateView(diceCollectionResult)
    }

    private fun updateView(diceCollectionResult: DiceCollectionResult) {
        presenter.update(diceResultViewModelProvider.mapDiceResult(diceCollectionResult))
    }

}
