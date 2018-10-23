package com.alekseyvalyakin.roleplaysystem.data.formula

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.Dice

class DiceNumber(
        val value: Dice
) : Expression {
    override fun getLength(): Int {
        return 1 + value.maxValue.toString().length
    }

    override fun evaluate(): Double {
        return value.getRndValue().toDouble()
    }
}