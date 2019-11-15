package com.alekseyvalyakin.roleplaysystem.ribs.game.active.transition

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameView
import com.uber.rib.core.*

@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")
abstract class DefaultActiveGameInternalDetachTransition< S : RouterNavigatorState>(
        private val view: ActiveGameView
) : RouterNavigator.DetachTransition<ViewRouter<*,*,*>, S> {

    override fun willDetachFromHost(router: ViewRouter<*,*,*>, previousState: S, newState: S?, isPush: Boolean) {
        view.getContentContainer().removeView(router.view)
    }
}