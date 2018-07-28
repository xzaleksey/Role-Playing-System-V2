package com.alekseyvalyakin.roleplaysystem.ribs.root

import com.alekseyvalyakin.roleplaysystem.ribs.auth.AuthBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.auth.AuthRouter
import com.uber.rib.core.DefaultAttachTransition
import com.uber.rib.core.DefaultDetachTransition
import com.uber.rib.core.RouterNavigatorFactory
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
        private val authBuilder: AuthBuilder,
        routerNavigatorFactory: RouterNavigatorFactory
) : ViewRouter<RootView, RootInteractor, RootBuilder.Component>(view, interactor, component) {
    private val router = routerNavigatorFactory.create<RootState>(this)!!
    private val authAttachTransition = object : DefaultAttachTransition<AuthRouter, RootState>(authBuilder, view) {}
    private val authDetachTransition = object : DefaultDetachTransition<AuthRouter, RootState>(view) {}

    fun attachAuth() {
        router.pushTransientState<AuthRouter>(RootState.AUTH, authAttachTransition, authDetachTransition)
    }

}
