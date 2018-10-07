package com.alekseyvalyakin.roleplaysystem.ribs.profile

import com.uber.rib.core.RibTestBasePlaceholder
import com.uber.rib.core.RouterHelper

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ProfileRouterTest : RibTestBasePlaceholder() {

  @Mock internal lateinit var component: ProfileBuilder.Component
  @Mock internal lateinit var interactor: ProfileInteractor
  @Mock internal lateinit var view: ProfileView

  private var router: ProfileRouter? = null

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    router = ProfileRouter(view, interactor, component, profileUserProvider)
  }

  /**
   * TODO: Delete this example and add real tests.
   */
  @Test
  fun anExampleTest_withSomeConditions_shouldPass() {
    // Use RouterHelper to drive your router's lifecycle.
    RouterHelper.attach(router!!)
    RouterHelper.detach(router!!)

    throw RuntimeException("Remove this test and add real tests.")
  }

}

