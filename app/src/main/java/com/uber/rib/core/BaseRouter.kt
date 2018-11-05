package com.uber.rib.core

import android.annotation.SuppressLint
import android.view.View

@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA", "LeakingThis")
open class BaseRouter<V : View, I : Interactor<*, out Router<I, C>>, RouterState : SerializableRouterNavigatorState,
        C : InteractorBaseComponent<I>>(view: V, interactor: I, component: C
) : ViewRouter<V, I, C>(view, interactor, component) {
    protected val restorableRouterNavigator = MyModernRouterNavigator(this)
    private val tempBundle = Bundle()

    @SuppressLint("VisibleForTests")
    override fun saveInstanceState(outState: Bundle) {
        val routersToSave: Set<Router<*, *>> = restorableRouterNavigator.getRouters() - children
        super.saveInstanceState(outState)
        restorableRouterNavigator.onSaveInstanceState(outState)
        val bundle = outState.getBundleExtra(KEY_CHILD_ROUTERS)!!
        for (router in routersToSave) {
            bundle.putBundleExtra(router.tag, tempBundle.getBundleExtra(router.tag))
        }
    }

    override fun dispatchAttach(savedInstanceState: Bundle?) {
        super.dispatchAttach(savedInstanceState)
        if (savedInstanceState != null) {
            restorableRouterNavigator.restoreState(savedInstanceState)
        }
    }

    public override fun detachChild(childRouter: Router<out Interactor<*, *>, out InteractorBaseComponent<*>>) {
        if (restorableRouterNavigator.getRouters().contains(childRouter)) {
            val bundle = Bundle()
            childRouter.saveInstanceState(bundle)
            tempBundle.putBundleExtra(childRouter.tag, bundle)
        }
        super.detachChild(childRouter)
    }

    open fun attachRib(attachInfo: AttachInfo<RouterState>) {

    }
}
