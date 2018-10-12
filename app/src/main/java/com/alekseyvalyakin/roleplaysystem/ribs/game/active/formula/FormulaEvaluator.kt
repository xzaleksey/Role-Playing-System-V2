package com.alekseyvalyakin.roleplaysystem.ribs.game.active.formula

import android.support.annotation.VisibleForTesting
import timber.log.Timber

class FormulaEvaluator(
        private val customParsers: List<FormulaParser> = emptyList()
) : ExpressionParser {

    private val formulaParsers = mutableListOf(
            NumberParser,
            OperationTypeParser,
            ExpressionStartParser,
            ExpressionEndParser,
            DiceParser).apply {
        addAll(customParsers)
    }

    override fun parse(string: String): Expression? {
        val list = mutableListOf<FormulaPart>()
        val startExpressionIndexes = mutableListOf<Int>()
        val endExpressionIndexes = mutableListOf<Int>()
        var currentFormulaInfo: FormulaInfo? = null
        var currentIndex = 0
        val formulaPreValidator = FormulaPreValidator(string)

        fun addFormulaPart(formulaPart: FormulaPart) {
            list.add(formulaPart)
            if (formulaPart == ExpressionStart) {
                startExpressionIndexes.add(list.lastIndex)
            } else if (formulaPart == ExpressionEnd) {
                endExpressionIndexes.add(list.lastIndex)
            }
        }

        for ((index, char) in string.withIndex()) {
            if (!formulaPreValidator.isValid(index, char)) {
                throw IllegalArgumentException("Formula not preValidated")
            }

            val nextIndex = index + 1
            var stringToParse = string.substring(currentIndex, nextIndex)
            val formulaInfo = parseInternal(stringToParse, currentFormulaInfo)

            fun checkIsFormulaEnded(formulaInfo: FormulaInfo?): Boolean {
                if (formulaInfo != null && isFormulaEnded(formulaInfo.formulaPart)) {

                    addFormulaPart(formulaInfo.formulaPart)
                    currentFormulaInfo = null
                    currentIndex = nextIndex
                    return true
                }
                return false
            }


            if (checkIsFormulaEnded(formulaInfo)) {
                continue
            } else if (formulaInfo != null) {
                // Update current formula
                currentFormulaInfo = formulaInfo
            } else if (currentFormulaInfo != null) {
                //formula part ended
                addFormulaPart(currentFormulaInfo!!.formulaPart)

                currentFormulaInfo = null
                currentIndex = index
                stringToParse = string.substring(index, nextIndex)

                val newFormula = parseInternal(stringToParse, currentFormulaInfo)
                if (!checkIsFormulaEnded(newFormula)) {
                    currentFormulaInfo = newFormula
                }
            }

        }

        if (currentFormulaInfo != null) {
            list.add(currentFormulaInfo!!.formulaPart)
            currentIndex = string.lastIndex
        }

        if (currentIndex < string.lastIndex) {
            throw IllegalArgumentException("Formula parsed partially")
        }

        return createExpression(FormulaResult(list, createExpressionIndexes(startExpressionIndexes, endExpressionIndexes)))
    }

    @VisibleForTesting
    fun createExpressionIndexes(startExpressionIndexes: MutableList<Int>,
                                endExpressionIndexes: MutableList<Int>): MutableList<ExpressionIndexes> {
        val result = mutableListOf<ExpressionIndexes>()


        for ((i, startIndex) in startExpressionIndexes.withIndex()) {
            val listIterator = endExpressionIndexes.listIterator()

            var j = 0
            while (listIterator.hasNext()) {
                var countOfExpressionStart = 0
                val endIndex = listIterator.next()

                if (endIndex < startIndex) {
                    continue
                }

                if (i == startExpressionIndexes.lastIndex) {
                    result.add(ExpressionIndexes(startIndex, endIndex))
                    listIterator.remove()
                } else {
                    var startExpressionIndex = i + 1
                    while (startExpressionIndex <= startExpressionIndexes.lastIndex) {
                        if (startExpressionIndexes[startExpressionIndex] < endIndex) {
                            countOfExpressionStart++
                        } else {
                            break
                        }
                        startExpressionIndex++
                    }

                    if (j == countOfExpressionStart) {
                        result.add(ExpressionIndexes(startIndex, endIndex))
                        listIterator.remove()
                        break
                    }
                }
                j++
            }
        }
        return result
    }

    private fun createExpression(formulaResult: FormulaResult): Expression? {
        return createCustomExpression(formulaResult, 0, formulaResult.formulaParts.lastIndex)
    }

    private fun createCustomExpression(formulaResult: FormulaResult, startIndex: Int, endIndex: Int): Expression {
        val formulaParts = formulaResult.formulaParts
        val complexOperation = ComplexOperation()

        var i = startIndex

        while (i <= endIndex) {
            val currentFormulaPart: FormulaPart = if (formulaParts[i] == ExpressionStart) {
                val expressionIndexes = formulaResult.startExpressionIndices.find { it.startIndex == i }
                val createCustomExpression = createCustomExpression(
                        formulaResult,
                        expressionIndexes!!.startIndex + 1,
                        expressionIndexes.endIndex - 1)
                i = expressionIndexes.endIndex + 1
                createCustomExpression
            } else {
                val formulaPart = formulaParts[i]
                i++
                formulaPart
            }

            when (currentFormulaPart) {
                is OperationType -> complexOperation.addOperationType(currentFormulaPart)
                is Expression -> complexOperation.addExpression(currentFormulaPart)
                else -> Timber.e("Unknown type")
            }
        }

        return complexOperation
    }

    private fun isFormulaEnded(formulaPart: FormulaPart): Boolean {
        return formulaPart !is Number && formulaPart !is DiceNumber
    }

    private fun parseInternal(string: String, currentFormulaInfo: FormulaInfo?): FormulaInfo? {
        if (currentFormulaInfo != null) {
            val formulaPart = currentFormulaInfo.currentParser.parse(string)
            if (formulaPart != null) {
                return currentFormulaInfo.copy(formulaPart = formulaPart)
            }
        }

        formulaParsers.forEach {
            val formulaPart = it.parse(string)
            if (formulaPart != null) {
                return FormulaInfo(formulaPart, it)
            }
        }

        return null
    }

    class FormulaPreValidator(val string: String) {
        private var startExpression: Int = 0
        private var endExpression: Int = 0

        fun isValid(index: Int, char: Char): Boolean {
            if (char == ExpressionStart.value) {
                startExpression++
            } else if (char == ExpressionEnd.value) {
                endExpression++
            }
            if (endExpression > startExpression) {
                return false
            }
            if (index == string.length - 1 && startExpression != endExpression) {
                return false
            }

            return true
        }

    }

    data class FormulaInfo(
            val formulaPart: FormulaPart,
            val currentParser: FormulaParser
    )

    data class ExpressionIndexes(
            val startIndex: Int,
            val endIndex: Int
    )

    data class FormulaResult(
            val formulaParts: List<FormulaPart>,
            val startExpressionIndices: List<ExpressionIndexes>
    )

}