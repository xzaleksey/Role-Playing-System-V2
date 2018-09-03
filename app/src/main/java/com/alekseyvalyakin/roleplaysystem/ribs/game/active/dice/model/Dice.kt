package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model

import java.io.Serializable

data class Dice(val maxValue: Int) : Comparable<Dice>, Serializable {

    fun getRndValue(): Int {
        return 1 + (maxValue * Math.random()).toInt()
    }

    override fun compareTo(other: Dice): Int {
        return this.maxValue - other.maxValue
    }

}