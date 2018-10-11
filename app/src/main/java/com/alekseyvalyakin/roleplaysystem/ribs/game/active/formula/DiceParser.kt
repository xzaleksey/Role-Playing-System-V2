package com.alekseyvalyakin.roleplaysystem.ribs.game.active.formula

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.Dice

object DiceParser : ExpressionParser {
    override fun parse(string: String): Expression? {
        if (!string.startsWith("d")) {
            return null
        }

        val maxValue = string.substring(1).toIntOrNull() ?: return null
        return DiceNumber(Dice(maxValue))
    }
}