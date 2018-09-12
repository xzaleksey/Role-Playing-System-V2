package com.alekseyvalyakin.roleplaysystem.ribs.main

import com.alekseyvalyakin.roleplaysystem.base.filter.FilterModel
import com.alekseyvalyakin.roleplaysystem.data.auth.AuthProvider
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
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
    @Inject
    lateinit var createEmptyGameObservableProvider: CreateEmptyGameObservableProvider

    private val filterRelay = BehaviorRelay.createDefault<FilterModel>(FilterModel())

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        presenter.observeUiEvents()
                .flatMap(this::handleEvent)
                .subscribeWithErrorLogging { }
                .addToDisposables()

        mainViewModelProvider.observeViewModel(filterRelay.toFlowable(BackpressureStrategy.LATEST))
                .observeOn(uiScheduler)
                .subscribeWithErrorLogging {
                    presenter.updateModel(it)
                }.addToDisposables()

        createEmptyGameObservableProvider.observeCreateGameModel()
                .observeOn(uiScheduler)
                .subscribeWithErrorLogging { createGameModel ->
                    when (createGameModel) {
                        is CreateEmptyGameObservableProvider.CreateGameModel.GameCreateSuccess -> {
                            mainRibListener.onMainRibEvent(MainRibListener.MainRibEvent.CreateGame(createGameModel.game))
                        }

                        is CreateEmptyGameObservableProvider.CreateGameModel.GameCreateFail -> {
                            presenter.showError(createGameModel.t.localizedMessage)
                        }
                    }
                }.addToDisposables()

    }

    private fun handleEvent(uiEvents: UiEvents): Observable<*> {
        val observable: Observable<*> = Observable.empty<Any>()

        when (uiEvents) {
            is UiEvents.SearchRightIconClick -> {
                presenter.showSearchContextMenu()
            }
            is UiEvents.SearchInput -> {
                val value = filterRelay.value
                filterRelay.accept(value.copy(previousQuery = value.query, query = uiEvents.text))
            }
            is UiEvents.FabClick -> {
                return Observable.fromCallable {
                    createEmptyGameObservableProvider.createEmptyGameModel()
                }
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

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface MainPresenter {
        fun observeUiEvents(): Observable<UiEvents>

        fun showSearchContextMenu()

        fun updateModel(model: MainViewModel)

        fun showError(message: String)

    }

    sealed class UiEvents {
        object SearchRightIconClick : UiEvents()
        object Logout : UiEvents()
        object FabClick : UiEvents()

        class SearchInput(val text: String) : UiEvents()

        class RecyclerItemClick(val item: IFlexible<*>) : UiEvents()

    }
}
