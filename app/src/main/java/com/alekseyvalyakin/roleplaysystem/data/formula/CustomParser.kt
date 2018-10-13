package com.alekseyvalyakin.roleplaysystem.data.formula

class CustomParser(
        private val string: String = "x1",
        private val value: Double
) : FormulaParser {

    override fun parse(string: String): FormulaPart? {
        if (string == this.string) {
            return Number(value)
        }

        return null
    }
}