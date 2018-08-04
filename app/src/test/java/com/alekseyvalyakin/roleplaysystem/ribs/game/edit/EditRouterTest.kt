package com.alekseyvalyakin.roleplaysystem.ribs.game.edit

import com.uber.rib.core.RibTestBasePlaceholder
import com.uber.rib.core.RouterHelper

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class EditRouterTest : RibTestBasePlaceholder() {

  @Mock internal lateinit var component: EditBuilder.Component
  @Mock internal lateinit var interactor: EditInteractor
  @Mock internal lateinit var view: EditView

  private var router: EditRouter? = null

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    router = EditRouter(view, interactor, component)
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

