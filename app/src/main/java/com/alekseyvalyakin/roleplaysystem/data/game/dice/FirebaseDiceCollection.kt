package com.alekseyvalyakin.roleplaysystem.data.game.dice

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasId
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.Dice
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceCollection
import com.google.firebase.firestore.Exclude
import java.util.*

class FirebaseDiceCollection(
        var dices: HashMap<String, Int> = HashMap(),
        @Exclude
        @set:Exclude
        @get:Exclude
        @Volatile
        override var id: String = ""
) : HasId {

    @Exclude
    fun mapToDiceCollection(): DiceCollection {
        val diceCollection = DiceCollection(id)

        for ((key, value) in dices) {
            diceCollection.addDices(Dice(Integer.parseInt(key)), value)
        }

        return diceCollection
    }

    companion object {

        fun newInstance(diceCollection: DiceCollection): FirebaseDiceCollection {
            val firebaseDiceCollection = FirebaseDiceCollection()
            firebaseDiceCollection.id = diceCollection.id

            for ((key, value) in diceCollection.getDices()) {
                firebaseDiceCollection.dices[key.maxValue.toString()] = value
            }
            return firebaseDiceCollection
        }
    }
}
