package com.uber.rib.core

import android.view.ViewGroup

@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")
abstract class DefaultAttachTransition<R : ViewRouter<*, out Interactor<*, *>,
        out InteractorBaseComponent<*>>, S : RouterNavigatorState, out B : BaseViewBuilder<*, R, *>>
(
        protected val builder: B,
        protected val view: ViewGroup
) : RouterNavigator.AttachTransition<R, S> {
    override fun buildRouter(): R {
        return builder.build(view)
    }

    override fun willAttachToHost(router: R, previousState: S?, newState: S, isPush: Boolean) {
        view.addView(router.view)
    }
}