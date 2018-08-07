package com.alekseyvalyakin.roleplaysystem.ribs.game.create

import com.alekseyvalyakin.roleplaysystem.data.game.Game
import com.uber.rib.core.RestorableRouter
import com.uber.rib.core.ViewRouter
import java.io.Serializable

/**
 * Adds and removes children of {@link CreateGameBuilder.CreateGameScope}.
 *
 */
class CreateGameRouter(
        view: CreateGameView,
        interactor: CreateGameInteractor,
        component: CreateGameBuilder.Component,
        val game: Game
) : ViewRouter<CreateGameView, CreateGameInteractor, CreateGameBuilder.Component>(view, interactor, component), RestorableRouter {

    override fun getRestorableInfo(): Serializable? {
        return game
    }

}
