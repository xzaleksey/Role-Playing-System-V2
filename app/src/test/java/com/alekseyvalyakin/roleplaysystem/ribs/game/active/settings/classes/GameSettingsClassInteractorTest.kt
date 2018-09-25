package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes

import com.uber.rib.core.RibTestBasePlaceholder
import com.uber.rib.core.InteractorHelper

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class GameSettingsClassInteractorTest : RibTestBasePlaceholder() {

  @Mock internal lateinit var presenter: GameSettingsClassPresenter
  @Mock internal lateinit var router: GameSettingsClassRouter

  private var interactor: GameSettingsClassInteractor? = null

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    interactor = TestGameSettingsClassInteractor.create(presenter)
  }

  /**
   * TODO: Delete this example and add real tests.
   */
  @Test
  fun anExampleTest_withSomeConditions_shouldPass() {
    // Use InteractorHelper to drive your interactor's lifecycle.
    InteractorHelper.attach<GameSettingsClassPresenter, GameSettingsClassRouter>(interactor!!, presenter, router, null)
    InteractorHelper.detach(interactor!!)

    throw RuntimeException("Remove this test and add real tests.")
  }
}