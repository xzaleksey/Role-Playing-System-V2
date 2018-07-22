package com.alekseyvalyakin.roleplaysystem.ribs.root

import com.alekseyvalyakin.roleplaysystem.data.auth.AuthProvider
import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import timber.log.Timber
import javax.inject.Inject

/**
 * Coordinates Business Logic for [RootScope].
 *
 * Base router
 */
@RibInteractor
class RootInteractor : Interactor<RootInteractor.RootPresenter, RootRouter>() {

    @Inject
    lateinit var presenter: RootPresenter
    @Inject
    lateinit var authProvider: AuthProvider


    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        val currentUser = authProvider.getCurrentUser()
        if (currentUser != null) {
            Timber.d(currentUser.email)
        }
        router.attachAuth()
    }

    override fun willResignActive() {
        super.willResignActive()

    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface RootPresenter
}
