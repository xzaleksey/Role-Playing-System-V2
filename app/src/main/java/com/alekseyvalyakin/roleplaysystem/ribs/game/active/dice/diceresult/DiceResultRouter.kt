package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceCollectionResult
import com.uber.rib.core.RestorableRouter
import com.uber.rib.core.ViewRouter
import java.io.Serializable

/**
 * Adds and removes children of {@link DiceResultBuilder.DiceResultScope}.
 *
 */
class DiceResultRouter(
        view: DiceResultView,
        interactor: DiceResultInteractor,
        component: DiceResultBuilder.Component,
        private val diceCollectionResult: DiceCollectionResult
) : ViewRouter<DiceResultView, DiceResultInteractor, DiceResultBuilder.Component>(view, interactor, component), RestorableRouter {

    override fun getRestorableInfo(): Serializable? {
        return diceCollectionResult
    }

}
