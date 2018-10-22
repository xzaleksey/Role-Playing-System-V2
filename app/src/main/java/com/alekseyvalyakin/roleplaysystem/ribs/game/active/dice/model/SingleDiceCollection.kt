package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model

import java.io.Serializable

data class SingleDiceCollection(
        val dice: Dice,
        private var count: Int = 0
) : Serializable {
    var id: String? = null

    @Synchronized
    fun addDices(count: Int = 1) {
        this.count += count
    }

    @Synchronized
    fun removeDices(count: Int = 1) {
        this.count = Math.max(0, this.count - count)
    }

    @Synchronized
    fun getDiceCount(): Int {
        return count
    }

    companion object {
        @JvmStatic
        fun createEmptySingleDiceCollectionFromDice(dice: Dice): SingleDiceCollection {
            return SingleDiceCollection(dice, 0)
        }

        @JvmStatic
        fun createSingleDiceCollectionList(): List<SingleDiceCollection> {
            return listOf(
                    DiceType.D4.createSingleDiceCollection(),
                    DiceType.D6.createSingleDiceCollection(),
                    DiceType.D8.createSingleDiceCollection(),
                    DiceType.D10.createSingleDiceCollection(),
                    DiceType.D12.createSingleDiceCollection(),
                    DiceType.D20.createSingleDiceCollection(),
                    DiceType.D100.createSingleDiceCollection()
            )
        }

    }

}