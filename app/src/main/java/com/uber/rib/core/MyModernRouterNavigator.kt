package com.uber.rib.core

import android.support.annotation.IntRange
import java.util.*

/**
 * Simple utility for switching a child router based on a state.
 *
 * @param <StateT> type of state to switch on.
</StateT> */
class MyModernRouterNavigator<StateT : RouterNavigatorState>(
        private val hostRouter: Router<*, *>
) : RestorableRouterNavigator<StateT> {

    private val navigationStack = ArrayDeque<RouterNavigator.RouterAndState<StateT>>()
    private val hostRouterName: String = hostRouter.javaClass.simpleName
    private var currentTransientRouterAndState: RouterNavigator.RouterAndState<StateT>? = null

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
        hostRouter.detachChild(fromRouterState.router)
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
        hostRouter.attachChild(toRouterState.router)
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
            hostRouter.attachChild(newRouter)
        }
    }

    override fun getRouters(): Set<Router<*, *>> {
        val result = mutableSetOf<Router<*, *>>()

        for (routerAndState in navigationStack) {
            result.add(routerAndState.router)
        }

        return result
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

    /** Writes out to the debug log.  */
    private fun log(text: String) {
        Rib.getConfiguration().handleDebugMessage("%s: $text", "RouterNavigator")
    }
}
