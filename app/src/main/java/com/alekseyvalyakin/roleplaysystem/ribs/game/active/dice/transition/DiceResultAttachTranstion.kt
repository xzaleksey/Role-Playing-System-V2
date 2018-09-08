package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.transition

import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.DiceRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult.DiceResultBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult.DiceResultRouter
import com.uber.rib.core.DefaultAttachTransition

class DiceResultAttachTranstion(
        diceResultBuilder: DiceResultBuilder,
        viewGroup: ViewGroup
) : DefaultAttachTransition<DiceResultRouter, DiceRouter.State, DiceResultBuilder>(
        diceResultBuilder,
        viewGroup
) {
    override fun willAttachToHost(router: DiceResultRouter, previousState: DiceRouter.State?, newState: DiceRouter.State, isPush: Boolean) {
        super.willAttachToHost(router, previousState, newState, isPush)
        router.view.z = 10f
    }

}