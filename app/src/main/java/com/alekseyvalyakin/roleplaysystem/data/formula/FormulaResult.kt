package com.alekseyvalyakin.roleplaysystem.data.formula

data class FormulaResult(
        val resultExpression: Expression?
) {
    fun isSuccess(): Boolean {
        return resultExpression != null
    }

    fun evaluate(): Double {
        return resultExpression?.evaluate() ?: 0.0
    }
}