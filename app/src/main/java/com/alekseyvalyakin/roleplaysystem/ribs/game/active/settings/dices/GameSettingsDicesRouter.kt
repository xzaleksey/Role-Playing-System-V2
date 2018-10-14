package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.dices

import com.uber.rib.core.ViewRouter

class GameSettingsDicesRouter(
        view: GameSettingsDicesView,
        interactor: GameSettingsDicesInteractor,
        component: GameSettingsDicesBuilder.Component) : ViewRouter<GameSettingsDicesView, GameSettingsDicesInteractor, GameSettingsDicesBuilder.Component>(view, interactor, component)
