package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

/**
 * Coordinates Business Logic for [GameSettingsClassScope].
 *
 */
@RibInteractor
class GameSettingsClassInteractor : BaseInteractor<GameSettingsClassPresenter, GameSettingsClassRouter>() {

    @Inject
    lateinit var presenter: GameSettingsClassPresenter
    @Inject
    lateinit var viewModelProvider: GameSettingsClassViewModelProvider
    @Inject
    lateinit var analyticsReporter: AnalyticsReporter
    @Inject
    lateinit var game: Game
    private val screenName = "GameSettingsClasses"

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        analyticsReporter.setCurrentScreen(screenName)

        viewModelProvider.observeViewModel()
                .subscribeWithErrorLogging {
                    presenter.update(it)
                }.addToDisposables()
    }

    override fun handleBackPress(): Boolean {
        return viewModelProvider.handleBackPress()
    }


}
