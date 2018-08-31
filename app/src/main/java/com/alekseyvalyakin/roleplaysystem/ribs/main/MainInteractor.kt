package com.alekseyvalyakin.roleplaysystem.ribs.main

import com.alekseyvalyakin.roleplaysystem.base.filter.FilterModel
import com.alekseyvalyakin.roleplaysystem.data.auth.AuthProvider
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.data.game.Game
import com.alekseyvalyakin.roleplaysystem.data.game.GameRepository
import com.alekseyvalyakin.roleplaysystem.di.activity.ThreadConfig
import com.alekseyvalyakin.roleplaysystem.flexible.FlexibleLayoutTypes
import com.alekseyvalyakin.roleplaysystem.flexible.game.GameListViewModel
import com.alekseyvalyakin.roleplaysystem.flexible.profile.UserProfileViewModel
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.jakewharton.rxrelay2.BehaviorRelay
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import eu.davidea.flexibleadapter.items.IFlexible
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
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var mainViewModelProvider: MainViewModelProvider
    @field:[Inject ThreadConfig(ThreadConfig.TYPE.UI)]
    lateinit var uiScheduler: Scheduler
    @Inject
    lateinit var mainRibListener: MainRibListener

    private val filterRelay = BehaviorRelay.createDefault<FilterModel>(FilterModel())

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        presenter.observeUiEvents()
                .concatMap(this::handleEvent)
                .subscribeWithErrorLogging { }
                .addToDisposables()

        mainViewModelProvider.observeViewModel(filterRelay.toFlowable(BackpressureStrategy.LATEST))
                .doOnSubscribe {
                    if (presenter.isEmpty()) {
                        presenter.showLoadingContent(true)
                    }
                }
                .observeOn(uiScheduler)
                .subscribeWithErrorLogging {
                    presenter.showLoadingContent(false)
                    presenter.updateModel(it)
                }.addToDisposables()
    }

    private fun handleEvent(uiEvents: UiEvents): Observable<*> {
        var observable: Observable<*> = Observable.empty<Any>()

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
            is UiEvents.RecyclerItemClick -> {
                return handleRecyclerViewItemClick(uiEvents.item)
            }

        }
        return observable
    }

    private fun handleRecyclerViewItemClick(item: IFlexible<*>): Observable<*> {
        when (item.layoutRes) {
            FlexibleLayoutTypes.USER_PROFILE -> {
                mainRibListener.onMainRibEvent(MainRibListener.MainRibEvent.MyProfile((item as UserProfileViewModel).user))
            }
            FlexibleLayoutTypes.GAME -> {
                (item as GameListViewModel).game.let {
                    Timber.d("Game clicked")
                    if (it.isDraft() && userRepository.isCurrentUser(it.masterId)) {
                        mainRibListener.onMainRibEvent(MainRibListener.MainRibEvent.CreateGame(it))
                    } else if (it.isActive()) {
                        mainRibListener.onMainRibEvent(MainRibListener.MainRibEvent.OpenActiveGame(it))
                    }
                }
            }
        }

        return Observable.empty<Any>()
    }

    private fun getCreateGameObservable(): Observable<Game> {
        return gameRepository.createDocument().toObservable()
                .doOnSubscribe { presenter.showFabLoading(true) }
                .onErrorReturn { t ->
                    Timber.e(t)
                    presenter.showError(t.localizedMessage)
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

        fun showError(message: String)

        fun showLoadingContent(loading: Boolean)

        fun isEmpty(): Boolean
    }

    sealed class UiEvents {
        object SearchRightIconClick : UiEvents()
        object Logout : UiEvents()
        object FabClick : UiEvents()

        class SearchInput(val text: String) : UiEvents()

        class RecyclerItemClick(val item: IFlexible<*>) : UiEvents()

    }
}
