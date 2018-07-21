package com.alekseyvalyakin.roleplaysystem.ribs.root

import com.alekseyvalyakin.roleplaysystem.ribs.auth.AuthBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.auth.AuthRouter
import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link RootBuilder.RootScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
class RootRouter(
        view: RootView,
        interactor: RootInteractor,
        component: RootBuilder.Component,
        private val authBuilder: AuthBuilder
) : ViewRouter<RootView, RootInteractor, RootBuilder.Component>(view, interactor, component) {
    private var authRouter: AuthRouter? = null

    fun attachAuth() {
        authRouter = authBuilder.build(view)
        attachChild(authRouter)
        view.addView(authRouter!!.view)
    }

    fun detachAuth() {
        authRouter?.let { detachChild(it) }
    }

}
