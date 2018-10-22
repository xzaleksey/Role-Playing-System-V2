package com.alekseyvalyakin.roleplaysystem.data.formula

class DndPartParser(
        private val string: String = "mx1",
        private val value: Double
) : FormulaPartParser {

    private val modifier = (value - value % 2 - 10) / 2

    override fun parse(string: String): FormulaPart? {
        if (string == this.string) {
            return Number(modifier)
        }

        return null
    }
}