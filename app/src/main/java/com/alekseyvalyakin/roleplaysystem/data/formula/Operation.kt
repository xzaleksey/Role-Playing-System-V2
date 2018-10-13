package com.alekseyvalyakin.roleplaysystem.data.formula

class Operation(
        val valueLeft: Expression? = null,
        val valueRight: Expression? = null,
        val operationType: OperationType
) : Expression, Comparable<Operation> {

    override fun evaluate(): Double {
        if (valueLeft != null && valueRight != null) {
            return when (operationType) {
                OperationType.DIVIDE -> {
                    val left = valueLeft.evaluate()
                    val right = valueRight.evaluate()
                    return left / right
                }
                OperationType.MULTIPLY -> {
                    val left = valueLeft.evaluate()
                    val right = valueRight.evaluate()
                    return left * right
                }
                OperationType.MINUS -> valueLeft.evaluate() - valueRight.evaluate()
                OperationType.PLUS -> valueLeft.evaluate() + valueRight.evaluate()
            }
        }
        if (valueRight != null) {
            return when (operationType) {
                OperationType.MINUS -> -valueRight.evaluate()
                else -> return valueRight.evaluate()
            }
        }

        return valueLeft!!.evaluate()
    }

    override fun compareTo(other: Operation): Int {
        return other.operationType.priority - operationType.priority
    }

}