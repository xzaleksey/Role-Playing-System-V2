package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats

import com.uber.rib.core.RibTestBasePlaceholder
import com.uber.rib.core.RouterHelper

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class GameSettingsStatRouterTest : RibTestBasePlaceholder() {

  @Mock internal lateinit var component: GameSettingsStatBuilder.Component
  @Mock internal lateinit var interactor: GameSettingsStatInteractor
  @Mock internal lateinit var view: GameSettingsStatView

  private var router: GameSettingsStatRouter? = null

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    router = GameSettingsStatRouter(view, interactor, component)
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

