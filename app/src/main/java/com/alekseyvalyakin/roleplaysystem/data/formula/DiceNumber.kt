package com.alekseyvalyakin.roleplaysystem.data.formula

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.Dice

class DiceNumber(
        val value: Dice
) : Expression {

    override fun evaluate(): Double {
        return value.getRndValue().toDouble()
    }
}