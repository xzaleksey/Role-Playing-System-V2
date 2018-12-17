package com.alekseyvalyakin.roleplaysystem.data.firestore.game.item

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.*
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

//TODO add attacking and defending abilities
data class FireStoreItem(
        @ServerTimestamp
        override var dateCreate: Date? = null,
        override var name: String =StringUtils.EMPTY_STRING,
        override var description: String =StringUtils.EMPTY_STRING,
        override var tags: MutableList<String> = mutableListOf(),

        var price: Double = 0.0,
        var weight: Int = 1,

        @Exclude
        @set:Exclude
        @get:Exclude
        override var id: String = StringUtils.EMPTY_STRING
) : FireStoreIdModel, HasDateCreate, HasName, HasDescription, HasTags
