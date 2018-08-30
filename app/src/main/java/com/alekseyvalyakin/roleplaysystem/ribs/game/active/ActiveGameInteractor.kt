package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

/**
 * Coordinates Business Logic for [ActiveGameScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class ActiveGameInteractor : Interactor<ActiveGameInteractor.ActiveGamePresenter, ActiveGameRouter>() {

  @Inject
  lateinit var presenter: ActiveGamePresenter

  override fun didBecomeActive(savedInstanceState: Bundle?) {
    super.didBecomeActive(savedInstanceState)

  }

  override fun willResignActive() {
    super.willResignActive()

  }

  /**
   * Presenter interface implemented by this RIB's view.
   */
  interface ActiveGamePresenter
}
