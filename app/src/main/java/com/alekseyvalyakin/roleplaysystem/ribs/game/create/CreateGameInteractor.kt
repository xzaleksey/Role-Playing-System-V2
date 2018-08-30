package com.alekseyvalyakin.roleplaysystem.ribs.game.create

import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityListener
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.model.CreateGameProvider
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.jakewharton.rxrelay2.BehaviorRelay
import com.uber.rib.core.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
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
    @Inject
    lateinit var createGameListener: CreateGameListener

    private val model: BehaviorRelay<CreateGameViewModel> = BehaviorRelay.create()

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        initModel(savedInstanceState)
        presenter.updateFabShowDisposable(model.toFlowable(BackpressureStrategy.LATEST).toObservable())
                .addToDisposables()

        presenter.observeUiEvents()
                .concatMap(this::handleEvent)
                .subscribeWithErrorLogging {}
                .addToDisposables()

        createGameProvider.observeGame(model.toFlowable(BackpressureStrategy.LATEST))
                .subscribeWithErrorLogging { }
                .addToDisposables()

        presenter.updateView(model.value)
    }

    private fun handleEvent(event: CreateGameUiEvent): Observable<*> {
        val value = model.value
        when (event) {
            is CreateGameUiEvent.InputChange -> {
                model.accept(value.copy(inputText = event.text))
            }
            is CreateGameUiEvent.ClickNext -> {
                val step = value.step
                val nextStep = step.getNextStep()
                if (nextStep == CreateGameStep.NONE) {
                    return createGameProvider.onChangeInfo(step, event.text).toObservable<Any>()
                            .doOnComplete {
                                createGameListener.onCreateGameEvent(CreateGameListener.CreateGameEvent.CompleteCreate(createGameProvider.getGame()))
                            }
                } else {
                    val newModel = viewModelProvider.getCreateGameViewModel(nextStep, createGameProvider.getGame())
                    model.accept(newModel)
                    presenter.updateView(newModel)
                    return createGameProvider.onChangeInfo(step, event.text).toObservable<Any>()
                }
            }
            is CreateGameUiEvent.BackPress -> {
                activityListener.backPress()
            }
        }
        return Observable.just(event)
    }

    override fun handleBackPress(): Boolean {
        val previousStep = model.value.step.getPreviousStep()
        if (previousStep == CreateGameStep.NONE) {
            return false
        }
        val newModel = viewModelProvider.getCreateGameViewModel(previousStep, createGameProvider.getGame())
        model.accept(newModel)
        presenter.updateView(newModel)
        return true
    }

    private fun initModel(savedInstanceState: Bundle?) {
        model.accept(savedInstanceState?.getSerializable(CreateGameViewModel.KEY)
                ?: viewModelProvider.getCreateGameViewModel(createGameProvider.getGame()))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(CreateGameViewModel.KEY, model.value)
    }

    override fun willResignActive() {
        super.willResignActive()
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface CreateGamePresenter {
        fun updateView(createGameViewModel: CreateGameViewModel)
        fun updateFabShowDisposable(viewModelObservable: Observable<CreateGameViewModel>): Disposable
        fun observeUiEvents(): Observable<CreateGameUiEvent>
    }
}
