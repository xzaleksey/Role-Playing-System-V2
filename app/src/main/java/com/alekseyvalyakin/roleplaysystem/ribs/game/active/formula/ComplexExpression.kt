package com.alekseyvalyakin.roleplaysystem.ribs.game.active.formula

class ComplexExpression(
        private val expressions: List<Operation> = listOf()
) : Expression {

    override fun evaluate(): Double {
        return expressions.asSequence().sortedDescending().sumByDouble { it.evaluate() }
    }

}