package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult

import com.uber.rib.core.RibTestBasePlaceholder
import com.uber.rib.core.InteractorHelper

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class DiceResultInteractorTest : RibTestBasePlaceholder() {

  @Mock internal lateinit var presenter: DiceResultInteractor.DiceResultPresenter
  @Mock internal lateinit var router: DiceResultRouter

  private var interactor: DiceResultInteractor? = null

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    interactor = TestDiceResultInteractor.create(presenter)
  }

  /**
   * TODO: Delete this example and add real tests.
   */
  @Test
  fun anExampleTest_withSomeConditions_shouldPass() {
    // Use InteractorHelper to drive your interactor's lifecycle.
    InteractorHelper.attach<DiceResultInteractor.DiceResultPresenter, DiceResultRouter>(interactor!!, presenter, router, null)
    InteractorHelper.detach(interactor!!)

    throw RuntimeException("Remove this test and add real tests.")
  }
}