package com.alekseyvalyakin.roleplaysystem.ribs.game.active.characters

import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

@RibInteractor
class GameCharactersInteractor : BaseInteractor<GameCharactersInteractor.GameCharactersPresenter, GameCharactersRouter>() {

  @Inject
  lateinit var presenter: GameCharactersPresenter

  override fun didBecomeActive(savedInstanceState: Bundle?) {
    super.didBecomeActive(savedInstanceState)

  }

  override fun willResignActive() {
    super.willResignActive()

  }

  /**
   * Presenter interface implemented by this RIB's view.
   */
  interface GameCharactersPresenter
}
