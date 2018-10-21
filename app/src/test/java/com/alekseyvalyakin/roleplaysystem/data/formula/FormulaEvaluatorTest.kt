package com.alekseyvalyakin.roleplaysystem.data.formula

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FormulaEvaluatorTest {

    @Test
    fun parseSum() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("5+5")
        assertEquals(10.0, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseSumDouble() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("5.2+5")
        assertEquals(10.2, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseMinus() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("5-7")
        assertEquals(-2.0, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseValueInParenthesis() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("(4)")
        assertEquals(4.0, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseUnMinus() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("-7")
        assertEquals(-7.0, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseMultiply() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("5*5")
        assertEquals(25.0, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseDivide() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("10/2")
        assertEquals(5.0, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseTwoOperationsSumAndMultiply() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("5+2*3")
        assertEquals(11.0, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseThreeOperations() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("5+2*3+5")
        assertEquals(16.0, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseThreeOperationsWithParentheses() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("5+2*(3+5)")
        assertEquals(21.0, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseThreeOperationsWithParentheses2() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("(5+2)*(3+5)")
        assertEquals(56.0, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseThreeOperationsWith3Parentheses() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("(2+1)*((3+5)*2)")
        assertEquals(48.0, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseDice() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("d6*5")
        val result = expression!!.evaluate()
        assertTrue(result <= 30)
        assertTrue(result >= 5)
    }

    @Test
    fun parseCustomParser() {
        val formulaEvaluator = FormulaEvaluator(listOf(
                CustomParser("x1", 5.0)
        ))

        val expression = formulaEvaluator.parse("x1")
        assertEquals(5.0, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseCustomParserDndDefault() {
        val formulaEvaluator = FormulaEvaluator(listOf(
                DndParser("mx1", 10.0)
        ))

        val expression = formulaEvaluator.parse("mx1")
        assertEquals(0.0, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseCustomParserDndDefaultNegative() {
        val formulaEvaluator = FormulaEvaluator(listOf(
                DndParser("mx1", 9.0)
        ))

        val expression = formulaEvaluator.parse("mx1")
        assertEquals(-1.0, expression!!.evaluate(), 0.0)
    }


    @Test
    fun parseSkillLevel() {
        val formulaEvaluator = FormulaEvaluator(listOf(
                SkillLevelParser(9.0)
        ))

        val expression = formulaEvaluator.parse("sl")
        assertEquals(9.0, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseCustomParserDndDefaultPositive() {
        val formulaEvaluator = FormulaEvaluator(listOf(
                DndParser("mx1", 11.0)
        ))

        val expression = formulaEvaluator.parse("mx1")
        assertEquals(0.0, expression!!.evaluate(), 0.0)
    }

    @Test
    fun parseCustomParserDndDefaultPositive2() {
        val formulaEvaluator = FormulaEvaluator(listOf(
                DndParser("mx1", 12.0)
        ))

        val expression = formulaEvaluator.parse("mx1")
        assertEquals(1.0, expression!!.evaluate(), 0.0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun parseEmptyExpression() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("")
        expression!!.evaluate()
    }

    @Test(expected = IllegalArgumentException::class)
    fun parseEmptyParenthesisExpression() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("()")
        expression!!.evaluate()
    }

    @Test(expected = IllegalArgumentException::class)
    fun parseWrongParenthesisExpression() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse(")5+4(")
        expression!!.evaluate()
    }

    @Test(expected = IllegalArgumentException::class)
    fun parseWrongParenthesisExpressionWithExpressionInside() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("(5+4())")
        expression!!.evaluate()
    }

    @Test(expected = IllegalArgumentException::class)
    fun parseTwoOperationsInRow() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("5++4")
        expression!!.evaluate()
    }

    @Test(expected = IllegalArgumentException::class)
    fun parseTwoExpressionsInRow() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("5d6+4")
        expression!!.evaluate()
    }

    @Test(expected = IllegalArgumentException::class)
    fun parseUnknownExpression() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("4x+5")
        expression!!.evaluate()
    }

    @Test(expected = IllegalArgumentException::class)
    fun parseUnknownExpression2() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("x2")
        expression!!.evaluate()
    }

    @Test(expected = IllegalArgumentException::class)
    fun parseInvalidNumber() {
        val formulaEvaluator = FormulaEvaluator()
        val expression = formulaEvaluator.parse("4..")
        expression!!.evaluate()
    }

    @Test
    fun createExpressionIndexesTriple() {
        val formulaEvaluator = FormulaEvaluator()

        val result = formulaEvaluator.createExpressionIndexes(
                mutableListOf(0, 1, 2),
                mutableListOf(6, 7, 8)
        )

        assertEquals(listOf(
                FormulaEvaluator.ExpressionIndexes(0, 8),
                FormulaEvaluator.ExpressionIndexes(1, 7),
                FormulaEvaluator.ExpressionIndexes(2, 6)
        ), result)

    }

    @Test
    fun createExpressionCouple() {
        val formulaEvaluator = FormulaEvaluator()

        val result = formulaEvaluator.createExpressionIndexes(
                mutableListOf(0, 3),
                mutableListOf(6, 7)
        )

        assertEquals(listOf(
                FormulaEvaluator.ExpressionIndexes(0, 7),
                FormulaEvaluator.ExpressionIndexes(3, 6)
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
                FormulaEvaluator.ExpressionIndexes(0, 1),
                FormulaEvaluator.ExpressionIndexes(2, 3),
                FormulaEvaluator.ExpressionIndexes(4, 5)
        )
        assertEquals(listExpected, result)
    }

    @Test
    fun formulaPreValidate() {
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

        assertEquals(expectedResult, result)
    }

    @Test
    fun formulaPreValidate1() {
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

        assertEquals(expectedResult, result)
    }


}