package com.alekseyvalyakin.roleplaysystem.data.formula

interface ExpressionParser : FormulaPartParser {
    override fun parse(string: String): Expression?
}