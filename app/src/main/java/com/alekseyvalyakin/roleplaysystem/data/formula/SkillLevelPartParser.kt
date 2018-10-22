package com.alekseyvalyakin.roleplaysystem.data.formula

class SkillLevelPartParser(
        private val value: Double
) : FormulaPartParser {
    private val string: String = "sl"

    override fun parse(string: String): FormulaPart? {
        if (string == this.string) {
            return Number(value)
        }

        return null
    }
}