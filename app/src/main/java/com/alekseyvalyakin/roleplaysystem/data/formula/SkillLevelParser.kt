package com.alekseyvalyakin.roleplaysystem.data.formula

class SkillLevelParser(
        private val value: Double
) : FormulaParser {
    private val string: String = "sl"

    override fun parse(string: String): FormulaPart? {
        if (string == this.string) {
            return Number(value)
        }

        return null
    }
}