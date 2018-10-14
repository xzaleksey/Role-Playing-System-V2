package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.equip

import com.uber.rib.core.ViewRouter

class GameSettingsEquipRouter(
        view: GameSettingsEquipView,
        interactor: GameSettingsEquipInteractor,
        component: GameSettingsEquipBuilder.Component) : ViewRouter<GameSettingsEquipView, GameSettingsEquipInteractor, GameSettingsEquipBuilder.Component>(view, interactor, component)
