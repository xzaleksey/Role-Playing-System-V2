package com.alekseyvalyakin.roleplaysystem.ribs.game.active.formula

interface FormulaParser {
    fun parse(string: String): FormulaPart?
}
