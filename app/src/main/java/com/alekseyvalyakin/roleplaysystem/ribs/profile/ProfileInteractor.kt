package com.alekseyvalyakin.roleplaysystem.ribs.profile

import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

/**
 * Coordinates Business Logic for [ProfileScope].
 *
 */
@RibInteractor
class ProfileInteractor : BaseInteractor<ProfileInteractor.ProfilePresenter, ProfileRouter>() {

  @Inject
  lateinit var presenter: ProfilePresenter

  override fun didBecomeActive(savedInstanceState: Bundle?) {
    super.didBecomeActive(savedInstanceState)

  }

  override fun willResignActive() {
    super.willResignActive()

  }

  /**
   * Presenter interface implemented by this RIB's view.
   */
  interface ProfilePresenter
}
