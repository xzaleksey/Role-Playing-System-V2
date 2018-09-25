package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link GameSettingsClassBuilder.GameSettingsClassScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
class GameSettingsClassRouter(
    view: GameSettingsClassView,
    interactor: GameSettingsClassInteractor,
    component: GameSettingsClassBuilder.Component) : ViewRouter<GameSettingsClassView, GameSettingsClassInteractor, GameSettingsClassBuilder.Component>(view, interactor, component)
