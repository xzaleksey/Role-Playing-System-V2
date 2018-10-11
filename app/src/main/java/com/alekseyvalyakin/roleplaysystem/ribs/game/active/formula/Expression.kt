package com.alekseyvalyakin.roleplaysystem.ribs.game.active.formula

interface Expression : FormulaPart {
    fun evaluate(): Double
}
