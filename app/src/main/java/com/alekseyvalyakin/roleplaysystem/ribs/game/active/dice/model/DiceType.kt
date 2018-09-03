package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model

import com.alekseyvalyakin.roleplaysystem.R

enum class DiceType(val resId: Int, private val dice: Dice) {
    D4(R.drawable.dice_d4, Dice(4)),
    D6(R.drawable.dice_d6, Dice(6)),
    D8(R.drawable.dice_d8, Dice(8)),
    D10(R.drawable.dice_d10, Dice(10)),
    D12(R.drawable.dice_d12, Dice(12)),
    D20(R.drawable.dice_d20, Dice(20));

    val maxValue: Int
        get() = dice.maxValue

    fun createDice(): Dice {
        return dice
    }

    fun createSingleDiceCollection(): SingleDiceCollection {
        return SingleDiceCollection.createEmptySingleDiceCollectionFromDice(createDice())
    }

    fun createDiceCollection(): DiceCollection {
        return DiceCollection.createDiceCollectionFromDice(createDice())
    }

    companion object {

        fun getDiceType(dice: Dice): DiceType {
            for (diceType in DiceType.values()) {
                if (diceType.dice == dice) {
                    return diceType
                }
            }

            return DiceType.D4
        }
    }
}