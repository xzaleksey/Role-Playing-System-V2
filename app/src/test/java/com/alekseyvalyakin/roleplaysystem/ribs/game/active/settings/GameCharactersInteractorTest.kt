package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings

import com.uber.rib.core.RibTestBasePlaceholder
import com.uber.rib.core.InteractorHelper

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class GameCharactersInteractorTest : RibTestBasePlaceholder() {

  @Mock internal lateinit var presenter: GameSettingsPresenter
  @Mock internal lateinit var router: GameSettingsRouter

  private var interactor: GameSettingsInteractor? = null

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    interactor = TestGameSettingsInteractor.create(presenter)
  }

  /**
   * TODO: Delete this example and add real tests.
   */
  @Test
  fun anExampleTest_withSomeConditions_shouldPass() {
    // Use InteractorHelper to drive your interactor's lifecycle.
    InteractorHelper.attach<GameSettingsPresenter, GameSettingsRouter>(interactor!!, presenter, router, null)
    InteractorHelper.detach(interactor!!)

    throw RuntimeException("Remove this test and add real tests.")
  }
}