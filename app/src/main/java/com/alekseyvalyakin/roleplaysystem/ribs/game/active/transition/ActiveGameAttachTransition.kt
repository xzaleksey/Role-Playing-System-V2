package com.alekseyvalyakin.roleplaysystem.ribs.game.active.transition

import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameParams
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameRouter
import com.uber.rib.core.RouterNavigator
import com.uber.rib.core.RouterNavigatorState
import com.uber.rib.core.ViewRouter

class ActiveGameAttachTransition<S : RouterNavigatorState>(
        val builder: ActiveGameBuilder,
        val view: ViewGroup,
        private val activeGameParams: ActiveGameParams
) : RouterNavigator.AttachTransition<ViewRouter<*,*,*>, S> {

    override fun buildRouter(): ViewRouter<*,*,*> {
        return builder.build(view, activeGameParams.game, activeGameParams)
    }

    override fun willAttachToHost(router: ViewRouter<*,*,*>, previousState: S?, newState: S, isPush: Boolean) {
        view.addView(router.view)
    }

}