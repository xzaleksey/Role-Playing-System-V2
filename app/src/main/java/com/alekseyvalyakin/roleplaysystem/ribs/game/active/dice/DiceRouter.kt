package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult.DiceResultBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult.DiceResultRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceCollectionResult
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.transition.DiceResultAttachTransition
import com.uber.rib.core.AttachInfo
import com.uber.rib.core.BaseRouter
import com.uber.rib.core.DefaultDetachTransition
import com.uber.rib.core.SerializableRouterNavigatorState
import java.io.Serializable

/**
 * Adds and removes children of {@link DiceBuilder.DiceScope}.
 *
 */
class DiceRouter(
        view: DiceView,
        interactor: DiceInteractor,
        component: DiceBuilder.Component,
        private val diceResultBuilder: DiceResultBuilder
) : BaseRouter<DiceView, DiceInteractor, DiceRouter.State, DiceBuilder.Component>(view, interactor, component) {

    private val resultDetachTransition = DefaultDetachTransition<DiceResultRouter, State>(view)

    fun attachDiceResult(diceCollectionResult: DiceCollectionResult) {
        attachRib(AttachInfo(State.RESULT(diceCollectionResult)))
    }

    override fun attachRib(attachInfo: AttachInfo<State>):Boolean {
        return internalPushTransientState(attachInfo.state,
                DiceResultAttachTransition(diceResultBuilder, view, attachInfo.state.getRestorableInfo() as DiceCollectionResult),
                resultDetachTransition)
    }

    fun backPress(): Boolean {
        if (peekState() == null) {
            return false
        }

        popState()
        return true
    }

    sealed class State(val name: String) : SerializableRouterNavigatorState {

        override fun name(): String {
            return name
        }

        class RESULT(val diceCollectionResult: DiceCollectionResult) : State("Result") {
            override fun getRestorableInfo(): Serializable? {
                return diceCollectionResult
            }
        }
    }
}
