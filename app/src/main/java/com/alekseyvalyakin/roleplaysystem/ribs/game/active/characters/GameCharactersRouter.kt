package com.alekseyvalyakin.roleplaysystem.ribs.game.active.characters

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link GameSettingsBuilder.GameSettingsScope}.
 *
 */
class GameCharactersRouter(
        view: GameCharactersView,
        interactor: GameCharactersInteractor,
        component: GameCharactersBuilder.Component) : ViewRouter<GameCharactersView, GameCharactersInteractor, GameCharactersBuilder.Component>(view, interactor, component)
