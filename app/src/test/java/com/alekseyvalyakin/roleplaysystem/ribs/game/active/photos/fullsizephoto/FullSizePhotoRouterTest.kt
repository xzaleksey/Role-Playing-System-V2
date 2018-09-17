package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.fullsizephoto

import com.uber.rib.core.RibTestBasePlaceholder
import com.uber.rib.core.RouterHelper

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class FullSizePhotoRouterTest : RibTestBasePlaceholder() {

  @Mock internal lateinit var component: FullSizePhotoBuilder.Component
  @Mock internal lateinit var interactor: FullSizePhotoInteractor
  @Mock internal lateinit var view: FullSizePhotoView

  private var router: FullSizePhotoRouter? = null

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    router = FullSizePhotoRouter(view, interactor, component)
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

