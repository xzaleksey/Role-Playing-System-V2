package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.fullsizephoto

import com.uber.rib.core.RibTestBasePlaceholder
import com.uber.rib.core.InteractorHelper

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class FullSizePhotoInteractorTest : RibTestBasePlaceholder() {

  @Mock internal lateinit var presenter: FullSizePhotoInteractor.FullSizePhotoPresenter
  @Mock internal lateinit var router: FullSizePhotoRouter

  private var interactor: FullSizePhotoInteractor? = null

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    interactor = TestFullSizePhotoInteractor.create(presenter)
  }

  /**
   * TODO: Delete this example and add real tests.
   */
  @Test
  fun anExampleTest_withSomeConditions_shouldPass() {
    // Use InteractorHelper to drive your interactor's lifecycle.
    InteractorHelper.attach<FullSizePhotoInteractor.FullSizePhotoPresenter, FullSizePhotoRouter>(interactor!!, presenter, router, null)
    InteractorHelper.detach(interactor!!)

    throw RuntimeException("Remove this test and add real tests.")
  }
}