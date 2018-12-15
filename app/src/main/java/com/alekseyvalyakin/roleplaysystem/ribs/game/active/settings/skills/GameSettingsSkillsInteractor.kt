package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
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
    lateinit var game: Game
    @Inject
    lateinit var gameSettingsSkillViewModelProvider: GameSettingsSkillViewModelProvider
    @Inject
    lateinit var analyticsReporter: AnalyticsReporter

    private val screenName = "GameSettingsSkills"

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        analyticsReporter.setCurrentScreen(screenName)

        gameSettingsSkillViewModelProvider.observeViewModel()
                .subscribeWithErrorLogging {
                    presenter.update(it)
                }.addToDisposables()
    }

    override fun handleBackPress(): Boolean {
        return gameSettingsSkillViewModelProvider.handleBackPress()
    }

}
