package com.alekseyvalyakin.roleplaysystem.data.formula

class DndParser(
        private val string: String = "mx1",
        private val value: Double
) : FormulaParser {

    private val modificator = (value - value % 2 - 10) / 2

    override fun parse(string: String): FormulaPart? {
        if (string == this.string) {
            return Number(modificator)
        }

        return null
    }
}