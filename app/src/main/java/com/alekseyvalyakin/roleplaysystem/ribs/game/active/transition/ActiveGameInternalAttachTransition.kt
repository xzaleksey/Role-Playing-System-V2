package com.alekseyvalyakin.roleplaysystem.ribs.game.active.transition

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameView
import com.uber.rib.core.RouterNavigator
import com.uber.rib.core.ViewRouter

open class ActiveGameInternalAttachTransition(
        private val routerCreator: RouterCreator<ViewRouter<*,*,*>>,
        val view: ActiveGameView
) : RouterNavigator.AttachTransition<ViewRouter<*,*,*>, ActiveGameRouter.State> {

    override fun buildRouter(): ViewRouter<*,*,*> {
        return routerCreator.createRouter(view)
    }

    override fun willAttachToHost(router: ViewRouter<*,*,*>, previousState: ActiveGameRouter.State?, newState: ActiveGameRouter.State, isPush: Boolean) {
        view.getContentContainer().addView(router.view)
    }

}