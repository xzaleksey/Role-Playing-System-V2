package com.alekseyvalyakin.roleplaysystem.ribs.game.active.formula

import org.junit.Test

class FormulaEvaluatorTest {


    @Test
    fun parse() {
        val formulaEvaluator = FormulaEvaluator()
        formulaEvaluator.parse("5+5")
    }

}