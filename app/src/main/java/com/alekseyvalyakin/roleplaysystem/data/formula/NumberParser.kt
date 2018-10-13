package com.alekseyvalyakin.roleplaysystem.data.formula

object NumberParser : ExpressionParser {
    override fun parse(string: String): Expression? {
        val value = string.toDoubleOrNull()
                ?: return null

        return Number(value)
    }
}