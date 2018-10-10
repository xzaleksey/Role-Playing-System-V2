package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills

import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

/**
 * Coordinates Business Logic for [GameSettingsSkillsScope].
 *
 */
@RibInteractor
class GameSettingsSkillsInteractor : BaseInteractor<GameSettingsSkillsPresenter, GameSettingsSkillsRouter>() {

    @Inject
    lateinit var presenter: GameSettingsSkillsPresenter

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
    }

}
