package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

/**
 * Coordinates Business Logic for [GameSettingsScope].
 *
 */
@RibInteractor
class GameSettingsInteractor : BaseInteractor<GameSettingsPresenter, GameSettingsRouter>() {

    @Inject
    lateinit var presenter: GameSettingsPresenter
    @Inject
    lateinit var gameSettingsViewModelProvider: GameSettingsViewModelProvider
    @Inject
    lateinit var game: Game


    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        presenter.update(gameSettingsViewModelProvider.getSettingsViewModel())
        presenter.observeUiEvents()
                .subscribeWithErrorLogging { uiEvent ->
                    when (uiEvent) {
                        is GameSettingsPresenter.UiEvent.GameSettingsClick -> {
                            router.attach(uiEvent.gameSettingsListViewModel.type)
                        }
                    }
                }
    }


    override fun handleBackPress(): Boolean {
        return router.onBackPressed()
    }
}
