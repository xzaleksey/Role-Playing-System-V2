package com.alekseyvalyakin.roleplaysystem.ribs.main

import com.uber.rib.core.RibTestBasePlaceholder
import com.uber.rib.core.InteractorHelper

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class MainInteractorTest : RibTestBasePlaceholder() {

  @Mock internal lateinit var presenter: MainInteractor.MainPresenter
  @Mock internal lateinit var router: MainRouter

  private var interactor: MainInteractor? = null

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    interactor = TestMainInteractor.create(presenter)
  }

  /**
   * TODO: Delete this example and add real tests.
   */
  @Test
  fun anExampleTest_withSomeConditions_shouldPass() {
    // Use InteractorHelper to drive your interactor's lifecycle.
    InteractorHelper.attach<MainInteractor.MainPresenter, MainRouter>(interactor!!, presenter, router, null)
    InteractorHelper.detach(interactor!!)

    throw RuntimeException("Remove this test and add real tests.")
  }
}