package com.alekseyvalyakin.roleplaysystem.ribs.root

import com.alekseyvalyakin.roleplaysystem.data.auth.AuthProvider
import com.alekseyvalyakin.roleplaysystem.di.activity.ThreadConfig
import com.alekseyvalyakin.roleplaysystem.ribs.abstractions.BaseInteractor
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import io.reactivex.Scheduler
import timber.log.Timber
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
        val currentUser = authProvider.getCurrentUser()
        if (currentUser != null) {
            Timber.d(currentUser.email)
        }
        authProvider.observeLoggedInState()
                .observeOn(uiScheduler)
                .subscribeWithErrorLogging { loggedIn ->
                    if (!loggedIn) {
                        router.attachAuth()
                    } else {
                        //TODO replace with enter
                        router.attachAuth()
                        Timber.d("logged In")
                    }
                }.addToDisposables()
    }

    override fun willResignActive() {
        super.willResignActive()

    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface RootPresenter
}
