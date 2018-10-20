package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.spells

import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

@RibInteractor
class GameSettingsSpellsInteractor : BaseInteractor<GameSettingsSpellsPresenter, GameSettingsSpellsRouter>() {

    @Inject
    lateinit var presenter: GameSettingsSpellsPresenter
    @Inject
    lateinit var analyticsReporter: AnalyticsReporter

    private val screenName = "GameSettingsSpells"

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        analyticsReporter.setCurrentScreen(screenName)
    }

}
