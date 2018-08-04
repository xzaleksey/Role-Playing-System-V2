package com.alekseyvalyakin.roleplaysystem.ribs.game.create

import com.uber.rib.core.RibTestBasePlaceholder
import com.uber.rib.core.RouterHelper

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class CreateGameRouterTest : RibTestBasePlaceholder() {

  @Mock internal lateinit var component: CreateGameBuilder.Component
  @Mock internal lateinit var interactor: CreateGameInteractor
  @Mock internal lateinit var view: CreateGameView

  private var router: CreateGameRouter? = null

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    router = CreateGameRouter(view, interactor, component)
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

