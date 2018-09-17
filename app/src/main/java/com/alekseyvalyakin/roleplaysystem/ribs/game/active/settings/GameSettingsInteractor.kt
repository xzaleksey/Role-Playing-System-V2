package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings

import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

/**
 * Coordinates Business Logic for [GameSettingsScope].
 *
 */
@RibInteractor
class GameSettingsInteractor : BaseInteractor<GameSettingsInteractor.GameSettingsPresenter, GameSettingsRouter>() {

  @Inject
  lateinit var presenter: GameSettingsPresenter

  override fun didBecomeActive(savedInstanceState: Bundle?) {
    super.didBecomeActive(savedInstanceState)

  }

  override fun willResignActive() {
    super.willResignActive()

  }

  /**
   * Presenter interface implemented by this RIB's view.
   */
  interface GameSettingsPresenter
}
