package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model

import java.io.Serializable

class DiceResult(val dice: Dice, var value: Int) : Serializable {

    @Synchronized
    fun rethrow() {
        value = dice.getRndValue()
    }

    companion object {
        fun throwDice(dice: Dice): DiceResult {
            return DiceResult(dice, dice.getRndValue())
        }
    }
}