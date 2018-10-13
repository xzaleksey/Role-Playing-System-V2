package com.alekseyvalyakin.roleplaysystem.data.formula

interface Expression : FormulaPart {
    fun evaluate(): Double
}
