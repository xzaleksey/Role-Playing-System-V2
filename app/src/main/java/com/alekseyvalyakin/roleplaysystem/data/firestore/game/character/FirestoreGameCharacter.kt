package com.alekseyvalyakin.roleplaysystem.data.firestore.game.character

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.*
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.value.StatHolder
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

data class FirestoreGameCharacter(
        @Exclude
        @set:Exclude
        @get:Exclude
        override var id: String = StringUtils.EMPTY_STRING,

        override var name: String,
        override var description: String,
        @ServerTimestamp override var dateCreate: Date? = null,
        override var ownerId: String = StringUtils.EMPTY_STRING,

        var stats: List<StatHolder> = emptyList(),
        var skills: List<FirestoreSkillHolder> = emptyList(),
        var classHolder: FirestoreClassHolder = FirestoreClassHolder(),
        var raceHolder: FirestoreRaceHolder = FirestoreRaceHolder(),

        var money: Double = 0.0,
        var age: Int = 25,
        var sex: String = Sex.MALE.text,
        var weight: Int = if (sex == Sex.MALE.text) 75 else 60

) : FireStoreIdModel, HasName, HasDescription, HasDateCreate, HasOwner

enum class Sex(var text: String) {
    MALE("male"),
    FEMALE("female");
}