package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import com.uber.rib.core.RibTestBasePlaceholder
import com.uber.rib.core.RouterHelper

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ActiveGameRouterTest : RibTestBasePlaceholder() {

  @Mock internal lateinit var component: ActiveGameBuilder.Component
  @Mock internal lateinit var interactor: ActiveGameInteractor
  @Mock internal lateinit var view: ActiveGameView

  private var router: ActiveGameRouter? = null

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    router = ActiveGameRouter(view, interactor, component)
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

