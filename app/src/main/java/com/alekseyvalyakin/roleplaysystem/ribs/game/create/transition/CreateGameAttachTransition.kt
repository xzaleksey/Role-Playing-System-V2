package com.alekseyvalyakin.roleplaysystem.ribs.game.create.transition

import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameRouter
import com.uber.rib.core.RouterNavigator
import com.uber.rib.core.RouterNavigatorState
import com.uber.rib.core.ViewRouter

class CreateGameAttachTransition<S : RouterNavigatorState>(
        val builder: CreateGameBuilder,
        val view: ViewGroup,
        val game: Game
) : RouterNavigator.AttachTransition<ViewRouter<*, *, *>, S> {

    override fun buildRouter(): ViewRouter<*,*,*> {
        return builder.build(view, game)
    }

    override fun willAttachToHost(router: ViewRouter<*,*,*>, previousState: S?, newState: S, isPush: Boolean) {
        view.addView(router.view)
    }

}