package com.alekseyvalyakin.roleplaysystem.ribs.game.create

import com.uber.rib.core.RibTestBasePlaceholder
import com.uber.rib.core.InteractorHelper

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class CreateGameInteractorTest : RibTestBasePlaceholder() {

  @Mock internal lateinit var presenter: CreateGameInteractor.CreateGamePresenter
  @Mock internal lateinit var router: CreateGameRouter

  private var interactor: CreateGameInteractor? = null

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    interactor = TestCreateGameInteractor.create(presenter)
  }

  /**
   * TODO: Delete this example and add real tests.
   */
  @Test
  fun anExampleTest_withSomeConditions_shouldPass() {
    // Use InteractorHelper to drive your interactor's lifecycle.
    InteractorHelper.attach<CreateGameInteractor.CreateGamePresenter, CreateGameRouter>(interactor!!, presenter, router, null)
    InteractorHelper.detach(interactor!!)

    throw RuntimeException("Remove this test and add real tests.")
  }
}