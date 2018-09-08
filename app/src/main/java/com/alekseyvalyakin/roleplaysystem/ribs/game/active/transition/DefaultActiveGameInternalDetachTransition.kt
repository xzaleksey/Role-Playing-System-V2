package com.alekseyvalyakin.roleplaysystem.ribs.game.active.transition

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameView
import com.uber.rib.core.*

@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")
abstract class DefaultActiveGameInternalDetachTransition<R : ViewRouter<*, out Interactor<*, *>, out InteractorBaseComponent<*>>, S : RouterNavigatorState>(
        private val view: ActiveGameView
) : RouterNavigator.DetachTransition<R, S> {

    override fun willDetachFromHost(router: R, previousState: S, newState: S?, isPush: Boolean) {
        view.getContentContainer().removeView(router.view)
    }
}