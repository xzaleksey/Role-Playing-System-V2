package com.alekseyvalyakin.roleplaysystem.ribs.game.active.formula

import org.junit.Assert
import org.junit.Test

class FormulaEvaluatorTest {

    @Test
    fun parseSum() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("5+5")
        Assert.assertEquals(10.0, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseSumDouble() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("5.2+5")
        Assert.assertEquals(10.2, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseMinus() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("5-7")
        Assert.assertEquals(-2.0, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseMultiply() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("5*5")
        Assert.assertEquals(25.0, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseDivide() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("10/2")
        Assert.assertEquals(5.0, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseTwoOperationsSumAndMultiply() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("5+2*3")
        Assert.assertEquals(11.0, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseThreeOperations() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("5+2*3+5")
        Assert.assertEquals(16.0, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseThreeOperationsWithParentheses() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("5+2*(3+5)")
        Assert.assertEquals(21.0, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseThreeOperationsWithParentheses2() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("(5+2)*(3+5)")
        Assert.assertEquals(56.0, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseDice() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("d6*5")
        val result = expression!!.evaluate()
        Assert.assertTrue(result <= 30)
        Assert.assertTrue(result >= 5)
    }

    @Test
    fun createExpressionIndexesTriple() {
        val formulaEvaluator = FormulaEvaluator()

        val result = formulaEvaluator.createExpressionIndexes(
                mutableListOf(0, 1, 2),
                mutableListOf(6, 7, 8)
        )

        Assert.assertEquals(listOf(
                FormulaEvaluator.ExperessionIndexes(0, 8),
                FormulaEvaluator.ExperessionIndexes(1, 7),
                FormulaEvaluator.ExperessionIndexes(2, 6)
        ), result)

    }

    @Test
    fun createExpressionCouple() {
        val formulaEvaluator = FormulaEvaluator()

        val result = formulaEvaluator.createExpressionIndexes(
                mutableListOf(0, 3),
                mutableListOf(6, 7)
        )

        Assert.assertEquals(listOf(
                FormulaEvaluator.ExperessionIndexes(0, 7),
                FormulaEvaluator.ExperessionIndexes(3, 6)
        ), result)
    }

    @Test
    fun createExpressionCoupleParallel() {
        val formulaEvaluator = FormulaEvaluator()

        val result = formulaEvaluator.createExpressionIndexes(
                mutableListOf(0, 2, 4),
                mutableListOf(1, 3, 5)
        )

        val listExpected = listOf(
                FormulaEvaluator.ExperessionIndexes(0, 1),
                FormulaEvaluator.ExperessionIndexes(2, 3),
                FormulaEvaluator.ExperessionIndexes(4, 5)
        )
        Assert.assertEquals(listExpected, result)
    }


    @Test
    fun formulaPrevalidate() {
        val string = ")("
        val formulaPreValidator = FormulaEvaluator.FormulaPreValidator(string)

        val expectedResult = false
        var result = true
        for ((index, c) in string.withIndex()) {
            result = formulaPreValidator.isValid(index, c)
            if (!result) {
                break
            }
        }

        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun formulaPrevalidate1() {
        val string = "("
        val formulaPreValidator = FormulaEvaluator.FormulaPreValidator(string)

        val expectedResult = false
        var result = true
        for ((index, c) in string.withIndex()) {
            result = formulaPreValidator.isValid(index, c)
            if (!result) {
                break
            }
        }

        Assert.assertEquals(expectedResult, result)
    }


}