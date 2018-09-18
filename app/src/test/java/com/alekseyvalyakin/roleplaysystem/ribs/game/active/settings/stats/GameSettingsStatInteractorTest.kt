package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats

import com.uber.rib.core.RibTestBasePlaceholder
import com.uber.rib.core.InteractorHelper

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class GameSettingsStatInteractorTest : RibTestBasePlaceholder() {

  @Mock internal lateinit var presenter: GameSettingsStatPresenter
  @Mock internal lateinit var router: GameSettingsStatRouter

  private var interactor: GameSettingsStatInteractor? = null

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    interactor = TestGameSettingsStatInteractor.create(presenter)
  }

  /**
   * TODO: Delete this example and add real tests.
   */
  @Test
  fun anExampleTest_withSomeConditions_shouldPass() {
    // Use InteractorHelper to drive your interactor's lifecycle.
    InteractorHelper.attach<GameSettingsStatPresenter, GameSettingsStatRouter>(interactor!!, presenter, router, null)
    InteractorHelper.detach(interactor!!)

    throw RuntimeException("Remove this test and add real tests.")
  }
}