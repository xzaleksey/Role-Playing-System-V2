package com.alekseyvalyakin.roleplaysystem.ribs.game.active.formula

interface FormulaPart {
}

object ExpressionStart : FormulaPart
object ExpressionEnd : FormulaPart

enum class OperationType(
        val value: String,
        val priority: Int
) : FormulaPart {
    PLUS("+", 1),
    MINUS("+", 1),
    DIVIDE("/", 2),
    MULTIPLY("*", 3);
}

object ExpressionStartParser : FormulaParser {

    override fun parse(string: String): FormulaPart? {
        return if (string == "(") ExpressionEnd else null
    }

}
object ExpressionEndParser : FormulaParser {
    override fun parse(string: String): FormulaPart? {
        return if (string == ")") ExpressionEnd else null
    }

}

object OperationTypeParser : FormulaParser {
    override fun parse(string: String): OperationType? {
        return OperationType.values().firstOrNull { it.value == string }
    }
}
