package com.alekseyvalyakin.roleplaysystem.data.firestore.game.dice

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreIdModel
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDateCreate
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.Dice
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.model.DiceCollection
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class FirestoreDiceCollection(
        var dices: HashMap<String, Int> = HashMap(),
        @ServerTimestamp override var dateCreate: Date? = null,

        @Exclude
        @set:Exclude
        @get:Exclude
        @Volatile
        override var id: String = StringUtils.EMPTY_STRING
) : FireStoreIdModel, HasDateCreate {

    @Exclude
    fun mapToDiceCollection(): DiceCollection {
        val diceCollection = DiceCollection(id)

        for ((key, value) in dices) {
            diceCollection.addDices(Dice(Integer.parseInt(key)), value)
        }

        return diceCollection
    }

    companion object {
        fun newInstance(diceCollection: DiceCollection): FirestoreDiceCollection {
            val firebaseDiceCollection = FirestoreDiceCollection()
            firebaseDiceCollection.id = diceCollection.id

            for ((key, value) in diceCollection.getDices()) {
                firebaseDiceCollection.dices[key.maxValue.toString()] = value
            }
            return firebaseDiceCollection
        }
    }
}
