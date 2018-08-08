package com.alekseyvalyakin.roleplaysystem.ribs.game.create

import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityListener
import com.alekseyvalyakin.roleplaysystem.ribs.game.model.CreateGameProvider
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import com.uber.rib.core.getSerializable
import com.uber.rib.core.putSerializable
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
    lateinit var createGameProvider: CreateGameProvider

    private lateinit var model: CreateGameViewModel

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        initModel(savedInstanceState)
        presenter.updateFabShowDisposable()
                .addToDisposables()
        presenter.observeUiEvents()
                .concatMap(this::handleEvent)
                .subscribeWithErrorLogging {}
                .addToDisposables()
        createGameProvider.observeGame()
                .subscribeWithErrorLogging { }
                .addToDisposables()
        presenter.updateView(model)
    }

    private fun handleEvent(event: CreateGameUiEvent): Observable<*> {
        when (event) {
            is CreateGameUiEvent.InputChange -> {
                model = model.copy(inputText = event.text)
            }
            is CreateGameUiEvent.ClickNext -> {
                val nextStep = model.step.getNextStep()
                if (nextStep == CreateGameStep.NONE) {
                    Timber.d("Final step")
                } else {
                    model = viewModelProvider.getCreateGameViewModel(nextStep, createGameProvider.getGame())
                    presenter.updateView(model)
                    return createGameProvider.onChangeInfo(model.step, event.text).toObservable<Any>()
                }
            }
            is CreateGameUiEvent.BackPress -> {
                activityListener.backPress()
            }
        }
        return Observable.just(event)
    }

    override fun handleBackPress(): Boolean {
        val previousStep = model.step.getPreviousStep()
        if (previousStep == CreateGameStep.NONE) {
            return false
        }
        model = viewModelProvider.getCreateGameViewModel(previousStep, createGameProvider.getGame())
        presenter.updateView(model)
        return true
    }

    private fun initModel(savedInstanceState: Bundle?) {
        model = savedInstanceState?.getSerializable(CreateGameViewModel.KEY)
                ?: viewModelProvider.getCreateGameViewModel(createGameProvider.getGame())
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
