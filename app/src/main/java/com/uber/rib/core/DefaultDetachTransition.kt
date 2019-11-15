package com.uber.rib.core

import android.view.ViewGroup

open class DefaultDetachTransition<S : RouterNavigatorState>(
        private val view: ViewGroup
) : RouterNavigator.DetachTransition<ViewRouter<*,*,*>, S> {

    override fun willDetachFromHost(router: ViewRouter<*,*,*>, previousState: S, newState: S?, isPush: Boolean) {
        view.removeView(router.view)
    }
}