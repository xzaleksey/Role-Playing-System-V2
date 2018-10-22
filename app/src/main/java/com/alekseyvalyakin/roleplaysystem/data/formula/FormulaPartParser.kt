package com.alekseyvalyakin.roleplaysystem.data.formula

interface FormulaPartParser {
    fun parse(string: String): FormulaPart?
}
