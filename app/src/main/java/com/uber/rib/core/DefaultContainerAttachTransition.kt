package com.uber.rib.core

import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.views.interfaces.HasContainerView
import org.jetbrains.anko.matchParent

abstract class DefaultContainerAttachTransition<
        S : RouterNavigatorState,
        out B : BaseViewBuilder<*, *>,
        V>
(
        protected val builder: B,
        protected val view: V
) : RouterNavigator.AttachTransition<ViewRouter<*,*,*>, S> where V : ViewGroup, V : HasContainerView {

    override fun buildRouter(): ViewRouter<*,*,*> {
        return builder.build(view)
    }

    override fun willAttachToHost(router: ViewRouter<*,*,*>, previousState: S?, newState: S, isPush: Boolean) {
        view.getContainerView().addView(router.view, matchParent, matchParent)
    }
}