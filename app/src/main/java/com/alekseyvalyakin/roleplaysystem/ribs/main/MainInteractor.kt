package com.alekseyvalyakin.roleplaysystem.ribs.main

import com.alekseyvalyakin.roleplaysystem.base.filter.FilterModel
import com.alekseyvalyakin.roleplaysystem.data.auth.AuthProvider
import com.alekseyvalyakin.roleplaysystem.data.game.Game
import com.alekseyvalyakin.roleplaysystem.data.game.GameRepository
import com.alekseyvalyakin.roleplaysystem.di.activity.ThreadConfig
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.jakewharton.rxrelay2.BehaviorRelay
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.Scheduler
import timber.log.Timber
import javax.inject.Inject

/**
 * Coordinates Business Logic for [MainBuilder.MainScope].
 *
 * Provides info about user, last games and all games
 */
@RibInteractor
class MainInteractor : BaseInteractor<MainInteractor.MainPresenter, MainRouter>() {

    @Inject
    lateinit var presenter: MainPresenter
    @Inject
    lateinit var authProvider: AuthProvider
    @Inject
    lateinit var gameRepository: GameRepository
    @Inject
    lateinit var mainViewModelProvider: MainViewModelProvider
    @field:[Inject ThreadConfig(ThreadConfig.TYPE.UI)]
    lateinit var uiScheduler: Scheduler
    @Inject
    lateinit var mainRibListener: MainRibListener

    private val filterRelay = BehaviorRelay.create<FilterModel>()

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        filterRelay.accept(FilterModel())

        presenter.observeUiEvents()
                .concatMap(this::handleEvent)
                .subscribeWithErrorLogging { }
                .addToDisposables()

        mainViewModelProvider.observeViewModel(filterRelay.toFlowable(BackpressureStrategy.LATEST))
                .observeOn(uiScheduler)
                .subscribeWithErrorLogging {
                    presenter.updateModel(it)
                }.addToDisposables()
    }

    private fun handleEvent(uiEvents: UiEvents): Observable<*> {
        Timber.d(uiEvents.toString())
        var observable: Observable<*> = Observable.just(uiEvents)

        when (uiEvents) {
            is UiEvents.SearchRightIconClick -> {
                presenter.showSearchContextMenu()
            }
            is UiEvents.SearchInput -> {
                val value = filterRelay.value
                filterRelay.accept(value.copy(previousQuery = value.query, query = uiEvents.text))
            }
            is UiEvents.FabClick -> {
                observable = getCreateGameObservable()
            }
            is UiEvents.Logout -> {
                return authProvider.signOut().toObservable<Any>()
            }

        }
        return observable
    }

    private fun getCreateGameObservable(): Observable<Game> {
        return gameRepository.createDraftGame().toObservable()
                .doOnSubscribe { presenter.showFabLoading(true) }
                .onErrorReturn {
                    Timber.e(it)
                    Game.EMPTY_GAME
                }
                .doOnNext { game ->
                    presenter.showFabLoading(false)
                    if (game != Game.EMPTY_GAME) {
                        Timber.d("end chain")
                        mainRibListener.onMainRibEvent(MainRibListener.MainRibEvent.CreateGame(game))
                    }
                }
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface MainPresenter {
        fun observeUiEvents(): Observable<UiEvents>

        fun showSearchContextMenu()

        fun updateModel(model: MainViewModel)
        fun showFabLoading(loading: Boolean)
    }

    sealed class UiEvents {
        class SearchRightIconClick : UiEvents()
        class Logout : UiEvents()
        class SearchInput(val text: String) : UiEvents()
        class FabClick : UiEvents()
    }
}
