package com.alekseyvalyakin.roleplaysystem.ribs.auth

import com.uber.rib.core.RibTestBasePlaceholder
import com.uber.rib.core.RouterHelper

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class NewRouterTest : RibTestBasePlaceholder() {

  @Mock internal lateinit var component: NewBuilder.Component
  @Mock internal lateinit var interactor: NewInteractor
  @Mock internal lateinit var view: NewView

  private var router: NewRouter? = null

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    router = NewRouter(view, interactor, component)
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

