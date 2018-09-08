package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult.DiceResultBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult.DiceResultRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceCollectionResult
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.transition.DiceResultAttachTransition
import com.uber.rib.core.DefaultDetachTransition
import com.uber.rib.core.RouterNavigatorFactory
import com.uber.rib.core.RouterNavigatorState
import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link DiceBuilder.DiceScope}.
 *
 */
class DiceRouter(
        view: DiceView,
        interactor: DiceInteractor,
        component: DiceBuilder.Component,
        private val diceResultBuilder: DiceResultBuilder,
        routerNavigatorFactory: RouterNavigatorFactory) : ViewRouter<DiceView, DiceInteractor, DiceBuilder.Component>(view, interactor, component) {

    private val router = routerNavigatorFactory.create<State>(this)!!
    private val resultDetachTransition = object : DefaultDetachTransition<DiceResultRouter, State>(view) {}

    fun attachDiceResult(diceCollectionResult: DiceCollectionResult) {
        router.pushTransientState(State.RESULT,
                DiceResultAttachTransition(diceResultBuilder, view, diceCollectionResult),
                resultDetachTransition)
    }

    fun backPress(): Boolean {
        if (router.peekState() == null) {
            return false
        }

        router.popState()
        return true
    }

    data class State(val name: String) : RouterNavigatorState {

        override fun name(): String {
            return name
        }

        companion object {
            val RESULT = State("Result")
        }
    }
}
