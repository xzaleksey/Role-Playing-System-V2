package com.uber.rib.core

import android.view.ViewGroup
import org.jetbrains.anko.matchParent

abstract class DefaultAttachTransition<
        S : RouterNavigatorState,
        out B : BaseViewBuilder<*, *>>
(
        protected val builder: B,
        protected val view: ViewGroup
) : RouterNavigator.AttachTransition<ViewRouter<*, *, *>, S> {

    override fun buildRouter(): ViewRouter<*, *, *> = builder.build(view)


    override fun willAttachToHost(router: ViewRouter<*, *, *>, previousState: S?, newState: S, isPush: Boolean) {
        view.addView(router.view, matchParent, matchParent)
    }
}