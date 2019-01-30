package com.uber.rib.core

import android.annotation.SuppressLint
import android.support.annotation.IntRange
import android.view.View
import timber.log.Timber
import java.io.Serializable
import java.util.*

@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA", "LeakingThis")
open class BaseRouter<V : View, I : Interactor<*, out Router<I, C>>, StateT : SerializableRouterNavigatorState,
        C : InteractorBaseComponent<I>>(view: V, interactor: I, component: C
) : ViewRouter<V, I, C>(view, interactor, component), RouterNavigator<StateT> {
    private val myTag = "MyModernRouter"
    private val tempBundle = Bundle()
    private val navigationStack = ArrayDeque<RouterNavigator.RouterAndState<StateT>>()
    private val hostRouterName: String = javaClass.simpleName
    private var currentTransientRouterAndState: RouterNavigator.RouterAndState<StateT>? = null

    @SuppressLint("VisibleForTests")
    override fun saveInstanceState(outState: Bundle) {
        val routersToSave: Set<Router<*, *>> = getRoutersToSave()
        super.saveInstanceState(outState)
        val bundle = outState.getBundleExtra(KEY_CHILD_ROUTERS)!!
        for (router in routersToSave) {
            val routerTag = getRouterTag(router)
            Timber.d("saved $routerTag")
            bundle.putBundleExtra(routerTag, tempBundle.getBundleExtra(routerTag))
        }
        outState.putSerializable(myTag, SavedState(
                navigationStack.toList().map { AttachInfo<StateT>(it.state, false) },
                currentTransientRouterAndState?.state?.run { AttachInfo(this, true) }
        ))
    }

    override fun dispatchAttach(savedInstanceState: Bundle?, tag: String?) {
        super.dispatchAttach(savedInstanceState, tag)
        if (savedInstanceState != null) {
            restoreState(savedInstanceState)
        }

        if (savedInstanceState == null) {
            currentTransientRouterAndState?.router?.run {
                this.dispatchAttach(savedInstanceState, getRouterTag(this))
            } ?:navigationStack.peek()?.router?.run {
                this.dispatchAttach(savedInstanceState, getRouterTag(this))
            }
        }
    }

    public override fun detachChild(childRouter: Router<out Interactor<*, *>, out InteractorBaseComponent<*>>) {
        if (navigationStack.indexOfFirst { it.router == childRouter } >= 0) {
            val bundle = Bundle()
            childRouter.saveInstanceState(bundle)
            val routerTag = getRouterTag(childRouter)
            Timber.d("saved  $routerTag")
            tempBundle.putBundleExtra(routerTag, bundle)
        }
        super.detachChild(childRouter)
    }

    open fun attachRib(attachInfo: AttachInfo<StateT>) {

    }

    init {
        log(String.format(
                Locale.getDefault(),
                "Installed new RouterNavigator: Hosting Router -> %s",
                hostRouterName)
        )
    }

    override fun popState() {
        // If we are in a transient state, go ahead and pop that state.
        var fromState: RouterNavigator.RouterAndState<StateT>? = null
        if (currentTransientRouterAndState != null) {
            fromState = currentTransientRouterAndState
            val fromRouterName = fromState!!.router.javaClass.simpleName
            currentTransientRouterAndState = null
            log(String.format(
                    Locale.getDefault(),
                    "Preparing to pop existing transient state for router: %s",
                    fromRouterName))
        } else {
            if (!navigationStack.isEmpty()) {
                fromState = navigationStack.pop()
                val fromRouterName = fromState!!.router.javaClass.simpleName
                log(String.format(
                        Locale.getDefault(),
                        "Preparing to pop existing state for router: %s",
                        fromRouterName))
            }
        }

        if (fromState != null) {
            // Pull the incoming state (So we can restore it.)
            var toState: RouterNavigator.RouterAndState<StateT>? = null
            if (!navigationStack.isEmpty()) {
                toState = navigationStack.peek()
            }

            detachInternal(fromState, toState, false)

            if (toState != null) {
                attachInternal(fromState, toState, false)
            }
        } else {
            log("No state to pop. No action will be taken.")
        }
    }

    override fun <R : Router<*, *>> pushRetainedState(
            newState: StateT,
            attachTransition: RouterNavigator.AttachTransition<R, StateT>,
            detachTransition: RouterNavigator.DetachTransition<R, StateT>?) {
        pushInternal(newState, attachTransition, detachTransition, false)
    }

    override fun <R : Router<*, *>> pushRetainedState(
            newState: StateT, attachTransition: RouterNavigator.AttachTransition<R, StateT>) {
        pushRetainedState(newState, attachTransition, null)
    }

    override fun <R : Router<*, *>> pushTransientState(
            newState: StateT,
            attachTransition: RouterNavigator.AttachTransition<R, StateT>,
            detachTransition: RouterNavigator.DetachTransition<R, StateT>?) {
        pushInternal(newState, attachTransition, detachTransition, true)
    }

    override fun <R : Router<*, *>> pushTransientState(
            newState: StateT, attachTransition: RouterNavigator.AttachTransition<R, StateT>) {
        pushTransientState(newState, attachTransition, null)
    }

    override fun peekRouter(): Router<*, *>? {
        val top = peekCurrentRouterAndState() ?: return null
        return top.router
    }

    override fun peekState(): StateT? {
        val top = peekCurrentRouterAndState() ?: return null
        return top.state
    }

    @IntRange(from = 0)
    override fun size(): Int {
        val stackSize = if (currentTransientRouterAndState == null) 0 else 1
        return navigationStack.size + stackSize
    }

    override fun hostWillDetach() {
        log(String.format(
                Locale.getDefault(), "Detaching RouterNavigator from host -> %s", hostRouterName))
        val currentRouterAndState = peekCurrentRouterAndState()
        detachInternal(currentRouterAndState, null as StateT?, false)
        currentTransientRouterAndState = null
        navigationStack.clear()
    }

    /**
     * Handles the detachment of a router.
     *
     * @param fromRouterState Previous state
     * @param toRouterState New state
     * @param isPush True if this is caused by a push
     */
    private fun detachInternal(
            fromRouterState: RouterNavigator.RouterAndState<StateT>?,
            toRouterState: RouterNavigator.RouterAndState<StateT>?,
            isPush: Boolean) {
        detachInternal(fromRouterState, toRouterState?.state, isPush)
    }

    /**
     * Handles the detachment of a router.
     *
     * @param fromRouterState Previous state
     * @param toState New state
     * @param isPush True if this is caused by a push
     */
    private fun detachInternal(
            fromRouterState: RouterNavigator.RouterAndState<StateT>?,
            toState: StateT?,
            isPush: Boolean) {
        if (fromRouterState == null) {
            log("No router to transition from. Call to detach will be dropped.")
            return
        }

        val callback = fromRouterState.detachCallback
        val fromRouterName = fromRouterState.router.javaClass.simpleName

        if (callback != null) {
            log(String.format(Locale.getDefault(), "Calling willDetachFromHost for %s", fromRouterName))
            callback.willDetachFromHost(
                    fromRouterState.router, fromRouterState.state, toState, isPush)
        }
        log(String.format(Locale.getDefault(), "Detaching %s from %s", fromRouterName, hostRouterName))
        detachChild(fromRouterState.router)
        if (callback != null) {
            log(
                    String.format(
                            Locale.getDefault(), "Calling onPostDetachFromHost for %s", fromRouterName))
            callback.onPostDetachFromHost(fromRouterState.router, toState, isPush)
        }
    }

    /**
     * Handles the attachment logic for a router.
     *
     * @param fromRouterState From router state.
     * @param toRouterState New state.
     * @param isPush True if this is from a push.
     */
    private fun attachInternal(
            fromRouterState: RouterNavigator.RouterAndState<StateT>?,
            toRouterState: RouterNavigator.RouterAndState<StateT>,
            isPush: Boolean) {
        val toRouterName = toRouterState.router.javaClass.simpleName
        val attachCallback = toRouterState.attachTransition

        log(String.format(Locale.getDefault(), "Calling willAttachToHost for %s", toRouterName))
        attachCallback.willAttachToHost(
                toRouterState.router,
                fromRouterState?.state,
                toRouterState.state,
                isPush)
        log(String.format(
                Locale.getDefault(), "Attaching %s as a child of %s", toRouterName, hostRouterName))
        val routerTag = getRouterTag(toRouterState.router)
        Timber.d("attached $routerTag")
        attachChild(toRouterState.router, routerTag)
    }

    private fun getRouterTag(router: Router<*, *>): String {
        val index = navigationStack.indexOfFirst { it.router == router }
        if (index >= 0) {
            return router::class.java.name + (navigationStack.size - 1 - index)
        }
        return router::class.java.name
    }

    private fun peekCurrentState(): StateT? {
        val currentRouterAndState = peekCurrentRouterAndState()
        return currentRouterAndState?.state
    }

    /**
     * Handles the pushing logic.
     *
     * @param newState New state
     * @param attachTransition Transition to use during attach.
     * @param detachTransition Detach transition to use during pop.
     * @param isTransient True if this is a transient entry.
     * @param <R> Router type.
    </R> */
    private fun <R : Router<*, *>> pushInternal(
            newState: StateT,
            attachTransition: RouterNavigator.AttachTransition<R, StateT>,
            detachTransition: RouterNavigator.DetachTransition<R, StateT>?,
            isTransient: Boolean) {
        val fromRouterAndState = peekCurrentRouterAndState()
        val fromState = peekCurrentState()
        if (fromState == null || fromState.name() != newState.name()) {
            if (fromRouterAndState != null) {
                detachInternal(fromRouterAndState, newState, true)
            }
            currentTransientRouterAndState = null

            val newRouter = attachTransition.buildRouter()
            log(String.format(Locale.getDefault(), "Built new router - %s", newRouter))
            attachTransition.willAttachToHost(newRouter, fromState, newState, true)
            val newRouterName = newRouter.javaClass.simpleName
            log(String.format(Locale.getDefault(), "Calling willAttachToHost for %s", newRouterName))

            val routerAndState = RouterNavigator.RouterAndState(newRouter, newState, attachTransition, detachTransition)

            if (isTransient) {
                currentTransientRouterAndState = routerAndState
            } else {
                navigationStack.push(routerAndState)
            }
            log(String.format(
                    Locale.getDefault(), "Attaching %s as a child of %s", newRouterName, hostRouterName))
            val routerTag = getRouterTag(newRouter)
            Timber.d("attached $routerTag")
            attachChild(newRouter, routerTag)
        }
    }

    private fun getRoutersToSave(): Set<Router<*, *>> {
        val result = mutableSetOf<Router<*, *>>()

        for (routerAndState in navigationStack) {
            if (!children.contains(routerAndState.router)) {
                result.add(routerAndState.router)
            }
        }

        return result
    }

    @SuppressLint("BinaryOperationInTimber")
    private fun restoreState(bundle: Bundle) {
        Timber.d("restore state")
        val savedState: SavedState<StateT> = bundle.getSerializable(myTag)
        for (attachInfo in savedState.stack.reversed()) {
            attachRib(attachInfo)
        }
        savedState.currentTransientState?.run {
            attachRib(this)
        }
    }

    private fun peekCurrentRouterAndState(): RouterNavigator.RouterAndState<StateT>? {
        return if (currentTransientRouterAndState != null) {
            currentTransientRouterAndState
        } else if (!navigationStack.isEmpty()) {
            navigationStack.peek()
        } else {
            null
        }
    }

    fun isEmptyStack() = navigationStack.isEmpty()

    open fun onBackPressed(): Boolean {
        val currentRouter = peekRouter()
        if (currentRouter != null && currentRouter.handleBackPress()) {
            return true
        }

        val handled = peekState() != null
        popState()
        return handled
    }


    /** Writes out to the debug log.  */
    private fun log(text: String) {
        Rib.getConfiguration().handleDebugMessage("%s: $text", "RouterNavigator")
    }

    data class SavedState<StateT : SerializableRouterNavigatorState>(
            val stack: List<AttachInfo<StateT>>,
            val currentTransientState: AttachInfo<StateT>? = null
    ) : Serializable
}
