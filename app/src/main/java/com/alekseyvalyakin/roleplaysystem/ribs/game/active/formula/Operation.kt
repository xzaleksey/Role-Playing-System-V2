package com.alekseyvalyakin.roleplaysystem.ribs.game.active.formula

class Operation(
        val valueLeft: Expression? = null,
        val valueRight: Expression? = null,
        val operationType: OperationType
) : Expression, Comparable<Operation> {

    override fun evaluate(): Double {
        if (valueLeft != null && valueRight != null) {
            return when (operationType) {
                OperationType.DIVIDE -> valueLeft.evaluate() / valueLeft.evaluate()
                OperationType.MULTIPLY -> valueLeft.evaluate() * valueRight.evaluate()
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