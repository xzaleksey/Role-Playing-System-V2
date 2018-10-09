package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreIdModel
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDateCreate
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class Restriction(
        @ServerTimestamp override var dateCreate: Date? = null,
        var restrictionType: Int = DependencyType.UNKNOWN.value,
        var restrictionId: String = StringUtils.EMPTY_STRING,

        @Exclude
        @set:Exclude
        @get:Exclude
        override var id: String
) : FireStoreIdModel, HasDateCreate

enum class RestrictionType(
        val value: Int
) {
    UNKNOWN(0),
    RACE(1),
    CLASS(2);

    companion object {
        fun getDependency(value: Int): RestrictionType {
            return values().firstOrNull { it.value == value } ?: UNKNOWN
        }
    }
}