package com.uber.rib.core

import android.view.View

@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA", "LeakingThis")
open class BaseRouter<V : View, I : Interactor<*, out Router<I, C>>, RouterState : RouterNavigatorState,
        C : InteractorBaseComponent<I>>(view: V, interactor: I, component: C
) : ViewRouter<V, I, C>(view, interactor, component) {
    protected val restorableRouterNavigator = MyModernRouterNavigator<RouterState>(this)

    override fun saveInstanceState(outState: Bundle) {
        val routersToSave: Set<Router<*, *>> = restorableRouterNavigator.getRouters() - children
        children.addAll(routersToSave)
        super.saveInstanceState(outState)
        children.removeAll(routersToSave)
    }
}