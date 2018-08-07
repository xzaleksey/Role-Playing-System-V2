package com.alekseyvalyakin.roleplaysystem.ribs.game.create.transition

import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.data.game.Game
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameRouter
import com.uber.rib.core.RouterNavigator
import com.uber.rib.core.RouterNavigatorState

class CreateGameAttachTransition<S : RouterNavigatorState>(
        val builder: CreateGameBuilder,
        val view: ViewGroup,
        val game: Game
) : RouterNavigator.AttachTransition<CreateGameRouter, S> {

    override fun buildRouter(): CreateGameRouter {
        return builder.build(view, game)
    }

    override fun willAttachToHost(router: CreateGameRouter, previousState: S?, newState: S, isPush: Boolean) {
        view.addView(router.view)
    }

}