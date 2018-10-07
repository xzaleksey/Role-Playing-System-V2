package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreIdModel
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDateCreate
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class Dependency(
        @ServerTimestamp override var dateCreate: Date? = null,
        var dependencyType: Int = DependencyType.UNKNOWN.value,
        var dependentId: String = StringUtils.EMPTY_STRING,

        @Exclude
        @set:Exclude
        @get:Exclude
        override var id: String
) : FireStoreIdModel, HasDateCreate {


}

enum class DependencyType(
        val value: Int
) {
    UNKNOWN(0),
    STAT(1),
    SKILL(2);

    companion object {
        fun getDependency(value: Int): DependencyType {
            return values().firstOrNull { it.value == value } ?: UNKNOWN
        }
    }
}