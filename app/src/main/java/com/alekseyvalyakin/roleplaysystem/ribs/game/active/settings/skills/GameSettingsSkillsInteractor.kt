package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills

import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
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
    @Inject
    lateinit var analyticsReporter: AnalyticsReporter

    private val screenName = "GameSettingsSkills"

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        analyticsReporter.setCurrentScreen(screenName)
    }

}
