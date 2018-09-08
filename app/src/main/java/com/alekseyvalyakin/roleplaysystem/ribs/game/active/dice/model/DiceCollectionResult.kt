package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model

import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import java.io.Serializable
import java.util.*

data class DiceCollectionResult(
        val id: String = StringUtils.EMPTY_STRING,
        private val diceResults: TreeMap<Dice, MutableList<DiceResult>> = TreeMap(),
        private var diceResultMax: Int = 0
) : Serializable {

    @Synchronized
    fun addDiceResult(diceResult: DiceResult) {
        val dice = diceResult.dice
        var dicesResultsList = diceResults[dice]
        diceResultMax += diceResult.dice.maxValue

        if (dicesResultsList == null) {
            dicesResultsList = mutableListOf()
            diceResults[dice] = dicesResultsList
        }

        dicesResultsList.add(diceResult)
    }

    @Synchronized
    fun getDiceResults(): Map<Dice, MutableList<DiceResult>> {
        return diceResults
    }

    @Synchronized
    fun getCurrentResult(): Int {
        var counter = 0
        for (diceResultList in diceResults.values) {
            for (diceResult in diceResultList) {
                counter += diceResult.value
            }
        }

        return counter
    }

    @Synchronized
    fun getMaxResult(): Int {
        return diceResultMax
    }

    @Synchronized
    fun resetResult() {
        diceResults.clear()
    }

    @Synchronized
    fun rethrow(){
        diceResults.values.forEach { dices -> dices.forEach { it.rethrow() } }
    }

    companion object {
        fun createResult(singleDiceCollections: List<SingleDiceCollection>): DiceCollectionResult {
            return DiceCollectionResult()
                    .apply {
                        singleDiceCollections.forEach { collection ->
                            repeat(collection.getDiceCount(), {
                                this.addDiceResult(DiceResult.throwDice(collection.dice))
                            })
                        }
                    }
        }
    }

}