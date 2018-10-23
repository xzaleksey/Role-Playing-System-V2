package com.alekseyvalyakin.roleplaysystem.data.formula

class Number(
        val value: Double
) : Expression, FormulaPart {
    override fun getLength(): Int {
        return value.toString().length
    }

    override fun evaluate(): Double {
        return value
    }
}