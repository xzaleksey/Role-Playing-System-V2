package com.alekseyvalyakin.roleplaysystem.ribs.game.active.transition

import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.data.game.Game
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameRouter
import com.uber.rib.core.RouterNavigator
import com.uber.rib.core.RouterNavigatorState

class ActiveGameAttachTransition<S : RouterNavigatorState>(
        val builder: ActiveGameBuilder,
        val view: ViewGroup,
        val game: Game
) : RouterNavigator.AttachTransition<ActiveGameRouter, S> {

    override fun buildRouter(): ActiveGameRouter {
        return builder.build(view, game)
    }

    override fun willAttachToHost(router: ActiveGameRouter, previousState: S?, newState: S, isPush: Boolean) {
        view.addView(router.view)
    }

}