package com.alekseyvalyakin.roleplaysystem.ribs.game.active.formula

interface ExpressionParser : FormulaParser {
    override fun parse(string: String): Expression?
}