package com.alekseyvalyakin.roleplaysystem.data.formula

class ComplexOperation : Expression {
    private val expressions = mutableListOf<Expression>()
    private val operationTypes = mutableListOf<OperationType>()
    private var lastAddedOperation = false
    private var isFirstOperation = false

    fun addExpression(expression: Expression) {
        lastAddedOperation = false
        expressions.add(expression)
        if (expressions.size - operationTypes.size > 1) {
            throw IllegalArgumentException("can' t be 2 expressions in a row")
        }
    }

    fun addOperationType(operationType: OperationType) {
        if (expressions.isEmpty()) {
            isFirstOperation = true
        }
        if (lastAddedOperation) {
            throw IllegalArgumentException("can' t be 2 operationsTypes in a row")
        }
        operationTypes.add(operationType)
        lastAddedOperation = true
    }

    override fun evaluate(): Double {
        val expressions = ArrayList(expressions)
        val operationTypes = ArrayList(operationTypes)

        while (operationTypes.isNotEmpty()) {
            val maxPriority = getMaxPriority(operationTypes)
            var currentIndex = 0
            for ((index, operationType) in operationTypes.withIndex()) {
                if (operationType == maxPriority) {
                    currentIndex = index
                    break
                }
            }

            val leftIndex = if (isFirstOperation) currentIndex - 1 else currentIndex
            val rightIndex = if (isFirstOperation) currentIndex else currentIndex + 1
            val leftValue = if (leftIndex < 0) null else expressions[leftIndex]
            val rightValue = expressions[rightIndex]
            val newValue = Operation(leftValue, rightValue, maxPriority).evaluate()
            expressions[rightIndex] = Number(newValue)
            leftValue?.run {
                expressions.removeAt(leftIndex)
            }
            operationTypes.removeAt(currentIndex)
        }
        if (expressions.isEmpty()) {
            throw IllegalArgumentException("Empty expression")
        }
        return expressions.first().evaluate()
    }

    private fun getMaxPriority(operationTypes: List<OperationType>): OperationType {
        return operationTypes.maxBy { it.priority }!!
    }
}