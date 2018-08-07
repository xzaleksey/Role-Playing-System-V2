package com.alekseyvalyakin.roleplaysystem.ribs.game.create

import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityListener
import com.alekseyvalyakin.roleplaysystem.ribs.game.model.GameProvider
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.uber.rib.core.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

/**
 * Coordinates Business Logic for [CreateGameScope].
 *
 */
@RibInteractor
class CreateGameInteractor : BaseInteractor<CreateGameInteractor.CreateGamePresenter, CreateGameRouter>() {

    @Inject
    lateinit var presenter: CreateGamePresenter
    @Inject
    lateinit var viewModelProvider: CreateGameViewModelProvider
    @Inject
    lateinit var activityListener: ActivityListener
    @Inject
    lateinit var gameProvider: GameProvider

    private lateinit var model: CreateGameViewModel

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        initModel(savedInstanceState)
        presenter.updateFabShowDisposable()
                .addToDisposables()
        presenter.observeUiEvents()
                .subscribeWithErrorLogging(this::handleEvent)
                .addToDisposables()
        gameProvider.observeGame()
                .subscribeWithErrorLogging { }
                .addToDisposables()
        presenter.updateView(model)
    }

    private fun handleEvent(event: CreateGameUiEvent) {
        when (event) {
            is CreateGameUiEvent.InputChange -> {
                model = model.copy(inputText = event.text)
            }
            is CreateGameUiEvent.ClickNext -> {
                Timber.d("Click next")
            }
            is CreateGameUiEvent.BackPress -> {
                activityListener.backPress()
            }
        }
    }

    override fun handleBackPress(): Boolean {
        val previousStep = model.step.getPreviousStep()
        if (previousStep == CreateGameStep.NONE) {
            return false
        }
        model = viewModelProvider.getCreateGameViewModel(previousStep, gameProvider.getGame())
        presenter.updateView(model)
        return true
    }

    private fun initModel(savedInstanceState: Bundle?) {
        model = savedInstanceState?.getSerializable(CreateGameViewModel.KEY)
                ?: viewModelProvider.getCreateGameViewModel(CreateGameStep.TITLE, gameProvider.getGame())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(CreateGameViewModel.KEY, model)
    }

    override fun willResignActive() {
        super.willResignActive()
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface CreateGamePresenter {
        fun updateView(createGameViewModel: CreateGameViewModel)
        fun updateFabShowDisposable(): Disposable
        fun observeUiEvents(): Observable<CreateGameUiEvent>
    }
}
