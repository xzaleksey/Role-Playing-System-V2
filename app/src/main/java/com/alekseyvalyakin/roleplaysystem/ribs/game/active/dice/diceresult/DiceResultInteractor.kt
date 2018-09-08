package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult

import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

/**
 * Coordinates Business Logic for [DiceResultScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class DiceResultInteractor : Interactor<DiceResultInteractor.DiceResultPresenter, DiceResultRouter>() {

  @Inject
  lateinit var presenter: DiceResultPresenter

  override fun didBecomeActive(savedInstanceState: Bundle?) {
    super.didBecomeActive(savedInstanceState)

  }

  override fun willResignActive() {
    super.willResignActive()

  }

  /**
   * Presenter interface implemented by this RIB's view.
   */
  interface DiceResultPresenter
}
