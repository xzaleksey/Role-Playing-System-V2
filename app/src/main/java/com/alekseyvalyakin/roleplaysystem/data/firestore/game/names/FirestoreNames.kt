package com.alekseyvalyakin.roleplaysystem.data.firestore.game.names

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreIdModel
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude
import kotlin.random.Random


data class FirestoreNames(
        val male: List<String> = emptyList(),
        val female: List<String> = emptyList(),

        @Exclude
        @set:Exclude
        @get:Exclude
        @Volatile
        override var id: String = StringUtils.EMPTY_STRING
) : FireStoreIdModel {

    @Exclude
    fun getMaleRandomName(): String {
        return male[Random.nextInt(male.size)]
    }

    @Exclude
    fun getFemaleRandomName(): String {
        return female[Random.nextInt(female.size)]
    }

}