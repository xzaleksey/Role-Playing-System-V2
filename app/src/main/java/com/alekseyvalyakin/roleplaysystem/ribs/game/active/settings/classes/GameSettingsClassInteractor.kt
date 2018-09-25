package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes

import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

/**
 * Coordinates Business Logic for [GameSettingsClassScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class GameSettingsClassInteractor : Interactor<GameSettingsClassPresenter, GameSettingsClassRouter>() {

  @Inject
  lateinit var presenter: GameSettingsClassPresenter

  override fun didBecomeActive(savedInstanceState: Bundle?) {
    super.didBecomeActive(savedInstanceState)

  }

  override fun willResignActive() {
    super.willResignActive()

  }

}
