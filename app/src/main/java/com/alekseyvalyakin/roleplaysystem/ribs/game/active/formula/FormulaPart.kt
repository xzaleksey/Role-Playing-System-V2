package com.alekseyvalyakin.roleplaysystem.ribs.game.active.formula

interface FormulaPart {
}

object ExpressionStart : FormulaPart {
    val value = '('
}

object ExpressionEnd : FormulaPart {
    val value = ')'
}

enum class OperationType(
        val value: String,
        val priority: Int
) : FormulaPart {
    PLUS("+", 1),
    MINUS("-", 1),
    DIVIDE("/", 2),
    MULTIPLY("*", 3);
}

object ExpressionStartParser : FormulaParser {

    override fun parse(string: String): FormulaPart? {
        return if (string == ExpressionStart.value.toString()) ExpressionStart else null
    }

}

object ExpressionEndParser : FormulaParser {
    override fun parse(string: String): FormulaPart? {
        return if (string == ExpressionEnd.value.toString()) ExpressionEnd else null
    }

}

object OperationTypeParser : FormulaParser {
    override fun parse(string: String): OperationType? {
        return OperationType.values().firstOrNull { it.value == string }
    }
}
