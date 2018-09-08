package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult

import com.uber.rib.core.RibTestBasePlaceholder
import com.uber.rib.core.RouterHelper

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class DiceResultRouterTest : RibTestBasePlaceholder() {

  @Mock internal lateinit var component: DiceResultBuilder.Component
  @Mock internal lateinit var interactor: DiceResultInteractor
  @Mock internal lateinit var view: DiceResultView

  private var router: DiceResultRouter? = null

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    router = DiceResultRouter(view, interactor, component)
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

