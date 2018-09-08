package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model

import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import java.io.Serializable
import java.util.*

data class DiceCollection(
        val id: String = StringUtils.EMPTY_STRING,
        private val dices: MutableMap<Dice, Int> = TreeMap()
) : Serializable {

    @Synchronized
    fun addDices(dice: Dice, count: Int = 1) {
        dices[dice] = count
    }

    @Synchronized
    fun removeDices(dice: Dice, count: Int = 1) {
        dices[dice]?.let {
            val newValue = it - count

            if (newValue >= 0) {
                dices[dice] = newValue
            }
        }
    }

    @Synchronized
    fun getDices(): Map<Dice, Int> {
        return dices
    }

    @Synchronized
    fun totalDices(): Int {
        return dices.values.sum()
    }

    @Synchronized
    fun getMaxValue(): Int {
        return dices.entries.sumBy { it.key.maxValue * it.value }
    }

    @Synchronized
    fun isSame(singleDiceCollections: List<SingleDiceCollection>): Boolean {
        return singleDiceCollections.none { it.getDiceCount() != dices[it.dice] ?: 0 }
    }

    @Synchronized
    fun toSingleDiceCollections(): List<SingleDiceCollection> {
        val result = mutableListOf<SingleDiceCollection>()
        for (value in DiceType.values()) {
            val dice = value.getDice()
            val diceCount = dices[dice]
            result.add(SingleDiceCollection(dice, diceCount ?: 0))
        }
        return result
    }

    companion object {
        @JvmStatic
        fun createDiceCollectionFromDice(dice: Dice): DiceCollection {
            val diceCollection = DiceCollection()
            diceCollection.addDices(dice, 0)
            return diceCollection
        }

        @JvmStatic
        fun createDiceCollectionFromSingleDiceCollections(singleDiceCollections: List<SingleDiceCollection>): DiceCollection {
            val diceCollection = DiceCollection()
            singleDiceCollections
                    .filter { it.getDiceCount() != 0 }
                    .forEach { diceCollection.addDices(it.dice, it.getDiceCount()) }
            return diceCollection
        }

    }
}