package com.alekseyvalyakin.roleplaysystem.ribs.game.create

import com.alekseyvalyakin.roleplaysystem.ribs.game.create.model.CreateGameProvider
import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link CreateGameBuilder.CreateGameScope}.
 *
 */
class CreateGameRouter(
        view: CreateGameView,
        interactor: CreateGameInteractor,
        component: CreateGameBuilder.Component,
        private val createGameProvider: CreateGameProvider
) : ViewRouter<CreateGameView, CreateGameInteractor, CreateGameBuilder.Component>(view, interactor, component)
