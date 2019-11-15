package com.uber.rib.core

import com.alekseyvalyakin.roleplaysystem.views.interfaces.HasContainerView

open class DefaultContainerDetachTransition<
        S : RouterNavigatorState,
        V : HasContainerView>(
        private val view: V
) : RouterNavigator.DetachTransition<ViewRouter<*,*,*>, S> {

    override fun willDetachFromHost(router: ViewRouter<*,*,*>, previousState: S, newState: S?, isPush: Boolean) {
        view.getContainerView().removeView(router.view)
    }
}