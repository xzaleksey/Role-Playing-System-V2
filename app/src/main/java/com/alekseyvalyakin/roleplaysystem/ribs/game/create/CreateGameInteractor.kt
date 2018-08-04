package com.alekseyvalyakin.roleplaysystem.ribs.game.create

import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

/**
 * Coordinates Business Logic for [CreateGameScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class CreateGameInteractor : Interactor<CreateGameInteractor.CreateGamePresenter, CreateGameRouter>() {

  @Inject
  lateinit var presenter: CreateGamePresenter

  override fun didBecomeActive(savedInstanceState: Bundle?) {
    super.didBecomeActive(savedInstanceState)

  }

  override fun willResignActive() {
    super.willResignActive()

  }

  /**
   * Presenter interface implemented by this RIB's view.
   */
  interface CreateGamePresenter
}
