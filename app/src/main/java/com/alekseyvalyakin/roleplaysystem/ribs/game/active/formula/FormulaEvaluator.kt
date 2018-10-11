package com.alekseyvalyakin.roleplaysystem.ribs.game.active.formula

import timber.log.Timber

class FormulaEvaluator() : ExpressionParser {

    private val formulaParsers = listOf(
            NumberParser,
            OperationTypeParser,
            ExpressionStartParser,
            ExpressionEndParser,
            DiceParser)

    override fun parse(string: String): Expression? {
        val list = mutableListOf<FormulaPart>()
        var currentFormulaPart: FormulaPart? = null
        var currentIndex = 0


        for ((index, _) in string.withIndex()) {

            val nextIndex = index + 1
            var stringToParse = string.substring(currentIndex, nextIndex)
            val formulaPart = parseInternal(stringToParse)

            fun checkIsFormulaEnded(formulaPart: FormulaPart?): Boolean {
                if (formulaPart != null && isFormulaEnded(formulaPart)) {

                    list.add(formulaPart)
                    currentFormulaPart = null
                    currentIndex = nextIndex
                    return true
                }
                return false
            }


            if (checkIsFormulaEnded(formulaPart)) {
                continue
            } else if (formulaPart != null) {
                // Update current formula
                currentFormulaPart = formulaPart
            } else if (currentFormulaPart != null) {
                list.add(currentFormulaPart!!)

                currentFormulaPart = null
                currentIndex = index
                stringToParse = string.substring(index, nextIndex)

                if (!checkIsFormulaEnded(parseInternal(stringToParse))) {
                    currentIndex++
                }
            }

        }

        if (currentFormulaPart != null) {
            list.add(currentFormulaPart!!)
        }

        Timber.d(list.toString())

        return null
    }

    private fun isFormulaEnded(formulaPart: FormulaPart): Boolean {
        return formulaPart !is Number && formulaPart !is DiceNumber
    }

    private fun parseInternal(string: String): FormulaPart? {
        formulaParsers.forEach {
            val formulaPart = it.parse(string)
            if (formulaPart != null) {
                return formulaPart
            }
        }
        return null
    }


}