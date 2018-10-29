package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreModel
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude

data class Dependency(
        var dependencyType: Int = DependencyType.UNKNOWN.value,
        var dependentId: String = StringUtils.EMPTY_STRING
) : FireStoreModel {

    @Exclude
    fun getDependencyType(): DependencyType {
        return DependencyType.getDependency(dependencyType)
    }

    @Exclude
    fun isValid(): Boolean {
        return DependencyType.getDependency(dependencyType) != DependencyType.UNKNOWN && dependentId.isNotBlank()
    }
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