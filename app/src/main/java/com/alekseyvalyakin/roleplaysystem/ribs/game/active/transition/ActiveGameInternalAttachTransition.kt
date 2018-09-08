package com.alekseyvalyakin.roleplaysystem.ribs.game.active.transition

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameView
import com.uber.rib.core.BaseViewBuilder
import com.uber.rib.core.RouterNavigator
import com.uber.rib.core.ViewRouter

@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")
class ActiveGameInternalAttachTransition<R : ViewRouter<*, *, *>,
        B : BaseViewBuilder<*, R, *>>(
        val builder: B,
        val view: ActiveGameView
) : RouterNavigator.AttachTransition<R, ActiveGameRouter.State> {

    override fun buildRouter(): R {
        return builder.build(view)
    }

    override fun willAttachToHost(router: R, previousState: ActiveGameRouter.State?, newState: ActiveGameRouter.State, isPush: Boolean) {
        view.getContentContainer().addView(router.view)
    }

}