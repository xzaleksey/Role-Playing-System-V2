package com.uber.rib.core

import android.view.ViewGroup

@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")
open class DefaultDetachTransition<R : ViewRouter<*, out Interactor<*, *>, out InteractorBaseComponent<*>>, S : RouterNavigatorState>(
        private val view: ViewGroup
) : RouterNavigator.DetachTransition<R, S> {

    override fun willDetachFromHost(router: R, previousState: S, newState: S?, isPush: Boolean) {
        view.removeView(router.view)
    }
}