package com.uber.rib.core

import com.alekseyvalyakin.roleplaysystem.views.interfaces.HasContainerView

@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")
open class DefaultContainerDetachTransition<R : ViewRouter<*, out Interactor<*, *>,
        out InteractorBaseComponent<*>>,
        S : RouterNavigatorState,
        V : HasContainerView>(
        private val view: V
) : RouterNavigator.DetachTransition<R, S> {

    override fun willDetachFromHost(router: R, previousState: S, newState: S?, isPush: Boolean) {
        view.getContainerView().removeView(router.view)
    }
}