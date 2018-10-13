package com.alekseyvalyakin.roleplaysystem.data.formula

class Number(
        val value: Double
) : Expression, FormulaPart {

    override fun evaluate(): Double {
        return value
    }
}