package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreModel
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils

data class Dependency(
        var dependencyType: Int = DependencyType.UNKNOWN.value,
        var dependentId: String = StringUtils.EMPTY_STRING
) : FireStoreModel

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