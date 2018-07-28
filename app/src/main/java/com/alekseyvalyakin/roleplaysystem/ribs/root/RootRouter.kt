package com.alekseyvalyakin.roleplaysystem.ribs.root

import com.alekseyvalyakin.roleplaysystem.ribs.auth.AuthBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.auth.AuthRouter
import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link RootBuilder.RootScope}.
 *
 * Root router of the app
 */
class RootRouter(
        view: RootView,
        interactor: RootInteractor,
        component: RootBuilder.Component,
        private val authBuilder: AuthBuilder
) : ViewRouter<RootView, RootInteractor, RootBuilder.Component>(view, interactor, component) {
    private var authRouter: AuthRouter? = null

    fun attachAuth() {
        if (authRouter == null) {
            authRouter = authBuilder.build(view)
            attachChild(authRouter)
            view.addView(authRouter!!.view)
        }
    }

    fun detachAuth() {
        if (authRouter != null) {
            authRouter?.let { detachChild(it) }
            authRouter = null
        }
    }

}
