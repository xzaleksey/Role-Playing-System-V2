package com.alekseyvalyakin.roleplaysystem.ribs.root

import com.alekseyvalyakin.roleplaysystem.data.auth.AuthProvider
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.di.activity.ThreadConfig
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameListener
import com.alekseyvalyakin.roleplaysystem.ribs.main.MainRibListener
import com.alekseyvalyakin.roleplaysystem.ribs.profile.ProfileListener
import com.alekseyvalyakin.roleplaysystem.utils.image.LocalImageProvider
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import io.reactivex.Observable
import io.reactivex.Scheduler
import javax.inject.Inject

/**
 * Coordinates Business Logic for [RootScope].
 *
 * Base router
 */
@RibInteractor
class RootInteractor : BaseInteractor<RootInteractor.RootPresenter, RootRouter>(), ProfileListener {
    @Inject
    lateinit var presenter: RootPresenter
    @Inject
    lateinit var authProvider: AuthProvider
    @field:[Inject ThreadConfig(ThreadConfig.TYPE.UI)]
    lateinit var uiScheduler: Scheduler
    @Inject
    lateinit var mainRibEventObservable: Observable<MainRibListener.MainRibEvent>
    @Inject
    lateinit var createGameObservable: Observable<CreateGameListener.CreateGameEvent>
    @Inject
    lateinit var localImageProvider: LocalImageProvider

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        authProvider.observeLoggedInState()
                .observeOn(uiScheduler)
                .subscribeWithErrorLogging { loggedIn ->
                    if (!loggedIn) {
                        router.attachAuth()
                    } else {
                        router.attachMain()
                    }
                }.addToDisposables()

        mainRibEventObservable
                .observeOn(uiScheduler)
                .subscribeWithErrorLogging { event ->
                    when (event) {
                        is MainRibListener.MainRibEvent.OpenGame -> {
                            router.attachGame(event.game)
                        }
                        is MainRibListener.MainRibEvent.MyProfile -> {
                            router.attachMyProfile(event.user)
                        }
                        is MainRibListener.MainRibEvent.NavigateToFeatures -> {
                            router.attachMyFeatures()
                        }
                        is MainRibListener.MainRibEvent.NavigateToLicense -> {
                            router.attachLicense()
                        }
                    }
                }.addToDisposables()

        createGameObservable
                .observeOn(uiScheduler)
                .subscribeWithErrorLogging { event ->
                    when (event) {
                        is CreateGameListener.CreateGameEvent.CompleteCreate -> {
                            router.detachCreateGame()
                            router.attachGame(event.game, true)
                        }
                    }
                }.addToDisposables()

        localImageProvider.subscribe().addToDisposables()
    }

    override fun openGame(game: Game) {
        router.attachGame(game)
    }

    override fun handleBackPress(): Boolean {
        return router.onBackPressed()
    }
    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface RootPresenter
}
