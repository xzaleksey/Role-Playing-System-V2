package com.alekseyvalyakin.roleplaysystem.data.formula

class CustomPartParser(
        private val string: String = "x1",
        private val value: Double = 0.0
) : FormulaPartParser {

    override fun parse(string: String): FormulaPart? {
        if (string == this.string) {
            return Number(value)
        }

        return null
    }

    sealed class Type(val text: String) {
        object CurrentObjectLevel : Type("l")
        object ClassLevel : Type("cl")
        object CharacterLevel : Type("ul")
        class Dependency(index: Int) : Type("x$index")
    }
}