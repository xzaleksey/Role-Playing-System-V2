package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats

import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityListener
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
    lateinit var activityListener: ActivityListener

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        presenter.observeUiEvents()
                .subscribeWithErrorLogging { uiEvent ->
                    when (uiEvent) {
                        is GameSettingsStatPresenter.UiEvent.ToolbarLeftClick -> {
                            activityListener.backPress()
                        }
                    }
                }.addToDisposables()
    }

    override fun willResignActive() {
        super.willResignActive()
    }

}
