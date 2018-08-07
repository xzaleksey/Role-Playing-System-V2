package com.alekseyvalyakin.roleplaysystem.ribs.root

import com.alekseyvalyakin.roleplaysystem.data.auth.AuthProvider
import com.alekseyvalyakin.roleplaysystem.data.game.Game
import com.alekseyvalyakin.roleplaysystem.di.activity.ThreadConfig
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameRouter
import com.alekseyvalyakin.roleplaysystem.ribs.main.MainRibListener
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.RibInteractor
import com.uber.rib.core.Router
import io.reactivex.Observable
import io.reactivex.Scheduler
import timber.log.Timber
import java.io.Serializable
import javax.inject.Inject

/**
 * Coordinates Business Logic for [RootScope].
 *
 * Base router
 */
@RibInteractor
class RootInteractor : BaseInteractor<RootInteractor.RootPresenter, RootRouter>() {

    @Inject
    lateinit var presenter: RootPresenter
    @Inject
    lateinit var authProvider: AuthProvider
    @field:[Inject ThreadConfig(ThreadConfig.TYPE.UI)]
    lateinit var uiScheduler: Scheduler
    @Inject
    lateinit var mainRibEventObservable: Observable<MainRibListener.MainRibEvent>

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
                        is MainRibListener.MainRibEvent.CreateGame -> {
                            router.attachCreateGame(event.game)
                        }
                    }
                }.addToDisposables()
    }

    override fun willResignActive() {
        super.willResignActive()

    }

    override fun handleBackPress(): Boolean {
        return router.onBackPressed()
    }

    override fun <T : Router<out Interactor<*, *>, out InteractorBaseComponent<*>>> restoreRouter(clazz: Class<T>, childInfo: Serializable?) {
        if (clazz == CreateGameRouter::class.java) {
            Timber.d("Restored create game Router")
            router.attachCreateGame(childInfo as Game)
        }
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface RootPresenter
}
