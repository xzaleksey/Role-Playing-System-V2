package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.races

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link GameSettingsClassBuilder.GameSettingsClassScope}.
 *
 */
class GameSettingsRaceRouter(
        view: GameSettingsRaceView,
        interactor: GameSettingsRaceInteractor,
        component: GameSettingsRaceBuilder.Component) : ViewRouter<GameSettingsRaceView, GameSettingsRaceInteractor, GameSettingsRaceBuilder.Component>(view, interactor, component)
