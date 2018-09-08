package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.transition

import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.DiceRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult.DiceResultBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult.DiceResultRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceCollectionResult
import com.uber.rib.core.RouterNavigator
import org.jetbrains.anko.matchParent

class DiceResultAttachTransition(
        private val diceResultBuilder: DiceResultBuilder,
        private val viewGroup: ViewGroup,
        private val diceCollectionResult: DiceCollectionResult
) : RouterNavigator.AttachTransition<DiceResultRouter, DiceRouter.State> {

    override fun buildRouter(): DiceResultRouter {
        return diceResultBuilder.build(viewGroup, diceCollectionResult)
    }

    override fun willAttachToHost(router: DiceResultRouter, previousState: DiceRouter.State?, newState: DiceRouter.State, isPush: Boolean) {
        viewGroup.addView(router.view, matchParent, matchParent)
        router.view.z = 10f
    }

}