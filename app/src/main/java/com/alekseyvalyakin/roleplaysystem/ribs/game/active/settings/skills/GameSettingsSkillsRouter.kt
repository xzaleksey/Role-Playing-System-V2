package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills

import android.view.View

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link GameSettingsSkillsBuilder.GameSettingsSkillsScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
class GameSettingsSkillsRouter(
    view: GameSettingsSkillsView,
    interactor: GameSettingsSkillsInteractor,
    component: GameSettingsSkillsBuilder.Component) : ViewRouter<GameSettingsSkillsView, GameSettingsSkillsInteractor, GameSettingsSkillsBuilder.Component>(view, interactor, component)
