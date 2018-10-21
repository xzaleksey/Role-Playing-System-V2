package com.alekseyvalyakin.roleplaysystem.data.firestore.game.character

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreIdModel
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDateCreate
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDescription
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasName
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.value.StatHolder
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude
import java.util.*

data class FirestoreGameCharacter(
        @Exclude
        @set:Exclude
        @get:Exclude
        override var id: String = StringUtils.EMPTY_STRING,

        override var name: String,
        override var description: String,
        override var dateCreate: Date? = null,

        var stats: List<StatHolder> = emptyList(),
        var classHolder: FirestoreClassHolder = FirestoreClassHolder(),
        var raceHolder: FirestoreRaceHolder = FirestoreRaceHolder(),
        var money: Double = 0.0

) : FireStoreIdModel, HasName, HasDescription, HasDateCreate {
}