package com.alekseyvalyakin.roleplaysystem.data.formula

class CustomPartParser(
        private val string: String = "x1",
        private val value: Double
) : FormulaPartParser {

    override fun parse(string: String): FormulaPart? {
        if (string == this.string) {
            return Number(value)
        }

        return null
    }
}