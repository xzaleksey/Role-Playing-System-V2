package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link GameSettingsStatBuilder.GameSettingsStatScope}.
 *
 */
class GameSettingsStatRouter(
    view: GameSettingsStatView,
    interactor: GameSettingsStatInteractor,
    component: GameSettingsStatBuilder.Component) : ViewRouter<GameSettingsStatView, GameSettingsStatInteractor, GameSettingsStatBuilder.Component>(view, interactor, component)
