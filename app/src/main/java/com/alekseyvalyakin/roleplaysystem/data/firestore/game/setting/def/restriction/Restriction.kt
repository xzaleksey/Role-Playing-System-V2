package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.restriction

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreModel
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency.DependencyType
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils

data class Restriction(
        var restrictionType: Int = DependencyType.UNKNOWN.value,
        var restrictionId: String = StringUtils.EMPTY_STRING
) : FireStoreModel

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