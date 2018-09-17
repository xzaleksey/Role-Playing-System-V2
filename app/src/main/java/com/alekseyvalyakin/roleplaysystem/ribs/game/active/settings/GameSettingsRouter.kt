package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link GameSettingsBuilder.GameSettingsScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
class GameSettingsRouter(
    view: GameSettingsView,
    interactor: GameSettingsInteractor,
    component: GameSettingsBuilder.Component) : ViewRouter<GameSettingsView, GameSettingsInteractor, GameSettingsBuilder.Component>(view, interactor, component)
