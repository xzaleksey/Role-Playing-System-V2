package com.alekseyvalyakin.roleplaysystem.data.formula

interface ExpressionParser : FormulaParser {
    override fun parse(string: String): Expression?
}