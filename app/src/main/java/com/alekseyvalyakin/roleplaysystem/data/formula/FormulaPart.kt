package com.alekseyvalyakin.roleplaysystem.data.formula

interface FormulaPart {
    fun getLength(): Int
}

object ExpressionStart : FormulaPart {
    const val value = '('

    override fun getLength(): Int {
        return 1
    }
}

object ExpressionEnd : FormulaPart {
    const val value = ')'

    override fun getLength(): Int {
        return 1
    }
}

enum class OperationType(
        val value: String,
        val priority: Int
) : FormulaPart {
    PLUS("+", 1),
    MINUS("-", 1),
    DIVIDE("/", 2),
    MULTIPLY("*", 3);

    override fun getLength(): Int {
        return 1
    }
}

object ExpressionStartPartParser : FormulaPartParser {

    override fun parse(string: String): FormulaPart? {
        return if (string == ExpressionStart.value.toString()) ExpressionStart else null
    }

}

object ExpressionEndPartParser : FormulaPartParser {
    override fun parse(string: String): FormulaPart? {
        return if (string == ExpressionEnd.value.toString()) ExpressionEnd else null
    }

}

object OperationTypePartParser : FormulaPartParser {
    override fun parse(string: String): OperationType? {
        return OperationType.values().firstOrNull { it.value == string }
    }
}
