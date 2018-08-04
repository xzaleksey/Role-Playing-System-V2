package com.alekseyvalyakin.roleplaysystem.ribs.game.create

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link CreateGameBuilder.CreateGameScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
class CreateGameRouter(
    view: CreateGameView,
    interactor: CreateGameInteractor,
    component: CreateGameBuilder.Component) : ViewRouter<CreateGameView, CreateGameInteractor, CreateGameBuilder.Component>(view, interactor, component)
