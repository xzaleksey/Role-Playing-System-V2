package com.uber.rib.core

import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.views.interfaces.HasContainerView
import org.jetbrains.anko.matchParent

@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")
abstract class DefaultContainerAttachTransition<R : ViewRouter<*, out Interactor<*, *>,
        out InteractorBaseComponent<*>>, S : RouterNavigatorState, out B : BaseViewBuilder<*, R, *>,
        V>
(
        protected val builder: B,
        protected val view: V
) : RouterNavigator.AttachTransition<R, S> where V : ViewGroup, V : HasContainerView {

    override fun buildRouter(): R {
        return builder.build(view)
    }

    override fun willAttachToHost(router: R, previousState: S?, newState: S, isPush: Boolean) {
        view.getContainerView().addView(router.view, matchParent, matchParent)
    }
}