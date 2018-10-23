package com.alekseyvalyakin.roleplaysystem.data.formula

class Operation(
        private val valueLeft: Expression? = null,
        private val valueRight: Expression? = null,
        private val operationType: OperationType
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

    override fun getLength(): Int {
        val left = valueLeft?.getLength() ?: 0
        val right = valueRight?.getLength() ?: 0

        return 1 + left + right
    }

    override fun compareTo(other: Operation): Int {
        return other.operationType.priority - operationType.priority
    }

}