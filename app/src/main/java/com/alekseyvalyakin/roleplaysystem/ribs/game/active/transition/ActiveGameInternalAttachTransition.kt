package com.alekseyvalyakin.roleplaysystem.ribs.game.active.transition

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameView
import com.uber.rib.core.RouterNavigator
import com.uber.rib.core.ViewRouter

@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")
open class ActiveGameInternalAttachTransition<R : ViewRouter<*, *, *>>(
        private val routerCreator: RouterCreator<R>,
        val view: ActiveGameView
) : RouterNavigator.AttachTransition<R, ActiveGameRouter.State> {

    override fun buildRouter(): R {
        return routerCreator.createRouter(view)
    }

    override fun willAttachToHost(router: R, previousState: ActiveGameRouter.State?, newState: ActiveGameRouter.State, isPush: Boolean) {
        view.getContentContainer().addView(router.view)
    }

}