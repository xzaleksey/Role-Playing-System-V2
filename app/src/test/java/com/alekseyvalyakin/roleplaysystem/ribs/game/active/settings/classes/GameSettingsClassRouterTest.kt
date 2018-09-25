package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes

import com.uber.rib.core.RibTestBasePlaceholder
import com.uber.rib.core.RouterHelper

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class GameSettingsClassRouterTest : RibTestBasePlaceholder() {

  @Mock internal lateinit var component: GameSettingsClassBuilder.Component
  @Mock internal lateinit var interactor: GameSettingsClassInteractor
  @Mock internal lateinit var view: GameSettingsClassView

  private var router: GameSettingsClassRouter? = null

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    router = GameSettingsClassRouter(view, interactor, component)
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

