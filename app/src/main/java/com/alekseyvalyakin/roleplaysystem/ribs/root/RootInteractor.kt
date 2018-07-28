package com.alekseyvalyakin.roleplaysystem.ribs.root

import com.alekseyvalyakin.roleplaysystem.data.auth.AuthProvider
import com.alekseyvalyakin.roleplaysystem.di.activity.ThreadConfig
import com.alekseyvalyakin.roleplaysystem.ribs.auth.AuthRouter
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.uber.rib.core.*
import io.reactivex.Scheduler
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
    }

    override fun willResignActive() {
        super.willResignActive()

    }

    override fun <T : Router<out Interactor<*, *>, out InteractorBaseComponent<*>>> restoreRouter(clazz: Class<T>) {
        if (clazz == AuthRouter::class.java) {
            router.attachAuth()
        }
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface RootPresenter
}
