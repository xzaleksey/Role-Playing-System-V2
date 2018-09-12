package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos

import com.uber.rib.core.RibTestBasePlaceholder
import com.uber.rib.core.InteractorHelper

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class PhotoInteractorTest : RibTestBasePlaceholder() {

  @Mock internal lateinit var presenter: PhotoInteractor.PhotoPresenter
  @Mock internal lateinit var router: PhotoRouter

  private var interactor: PhotoInteractor? = null

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    interactor = TestPhotoInteractor.create(presenter)
  }

  /**
   * TODO: Delete this example and add real tests.
   */
  @Test
  fun anExampleTest_withSomeConditions_shouldPass() {
    // Use InteractorHelper to drive your interactor's lifecycle.
    InteractorHelper.attach<PhotoInteractor.PhotoPresenter, PhotoRouter>(interactor!!, presenter, router, null)
    InteractorHelper.detach(interactor!!)

    throw RuntimeException("Remove this test and add real tests.")
  }
}