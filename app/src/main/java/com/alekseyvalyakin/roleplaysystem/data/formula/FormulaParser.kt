package com.alekseyvalyakin.roleplaysystem.data.formula

interface FormulaParser {
    fun parse(string: String): FormulaPart?
}
