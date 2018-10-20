package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.dices

import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

@RibInteractor
class GameSettingsDicesInteractor : BaseInteractor<GameSettingsDicesPresenter, GameSettingsDicesRouter>() {

    @Inject
    lateinit var presenter: GameSettingsDicesPresenter
    @Inject
    lateinit var analyticsReporter: AnalyticsReporter

    private val screenName = "GameSettingsDices"

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        analyticsReporter.setCurrentScreen(screenName)
    }

}
