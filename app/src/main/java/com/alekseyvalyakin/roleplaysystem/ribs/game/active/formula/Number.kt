package com.alekseyvalyakin.roleplaysystem.ribs.game.active.formula

class Number(
        val value: Double
) : Expression, FormulaPart {

    override fun evaluate(): Double {
        return value
    }
}