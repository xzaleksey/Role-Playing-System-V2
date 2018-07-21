package com.alekseyvalyakin.roleplaysystem.ribs.auth

import com.uber.rib.core.RibTestBasePlaceholder
import com.uber.rib.core.RouterHelper

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class AuthRouterTest : RibTestBasePlaceholder() {

  @Mock internal lateinit var component: AuthBuilder.Component
  @Mock internal lateinit var interactor: AuthInteractor
  @Mock internal lateinit var view: AuthView

  private var router: AuthRouter? = null

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    router = AuthRouter(view, interactor, component)
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

