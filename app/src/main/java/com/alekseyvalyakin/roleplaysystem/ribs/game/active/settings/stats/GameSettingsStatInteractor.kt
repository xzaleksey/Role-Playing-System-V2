package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats

import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

/**
 * Coordinates Business Logic for [GameSettingsStatScope].
 *
 */
@RibInteractor
class GameSettingsStatInteractor : BaseInteractor<GameSettingsStatPresenter, GameSettingsStatRouter>() {

    @Inject
    lateinit var presenter: GameSettingsStatPresenter
    @Inject
    lateinit var gameSettingsStatsViewModelProvider: GameSettingsStatsViewModelProvider

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        gameSettingsStatsViewModelProvider.observeViewModel()
                .subscribeWithErrorLogging {
                    presenter.update(it)
                }
                .addToDisposables()
    }

}
