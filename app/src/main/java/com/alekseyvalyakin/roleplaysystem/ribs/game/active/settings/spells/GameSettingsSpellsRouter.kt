package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.spells

import com.uber.rib.core.ViewRouter

class GameSettingsSpellsRouter(
        view: GameSettingsSpellsView,
        interactor: GameSettingsSpellsInteractor,
        component: GameSettingsSpellsBuilder.Component) : ViewRouter<GameSettingsSpellsView, GameSettingsSpellsInteractor, GameSettingsSpellsBuilder.Component>(view, interactor, component)
