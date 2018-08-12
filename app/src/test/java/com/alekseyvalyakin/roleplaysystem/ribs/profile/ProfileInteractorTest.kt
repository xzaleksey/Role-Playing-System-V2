package com.alekseyvalyakin.roleplaysystem.ribs.profile

import com.uber.rib.core.RibTestBasePlaceholder
import com.uber.rib.core.InteractorHelper

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ProfileInteractorTest : RibTestBasePlaceholder() {

  @Mock internal lateinit var presenter: ProfileInteractor.ProfilePresenter
  @Mock internal lateinit var router: ProfileRouter

  private var interactor: ProfileInteractor? = null

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    interactor = TestProfileInteractor.create(presenter)
  }

  /**
   * TODO: Delete this example and add real tests.
   */
  @Test
  fun anExampleTest_withSomeConditions_shouldPass() {
    // Use InteractorHelper to drive your interactor's lifecycle.
    InteractorHelper.attach<ProfileInteractor.ProfilePresenter, ProfileRouter>(interactor!!, presenter, router, null)
    InteractorHelper.detach(interactor!!)

    throw RuntimeException("Remove this test and add real tests.")
  }
}