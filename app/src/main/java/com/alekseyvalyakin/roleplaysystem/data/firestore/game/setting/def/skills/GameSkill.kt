package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills

import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreIdModel
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.Selectable
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency.Dependency
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency.Restriction
import com.google.firebase.firestore.Exclude


interface GameSkill : FireStoreIdModel, Selectable {
    @Exclude
    fun getDisplayedName(): String

    @Exclude
    fun getDisplayedDescription(): String

    @Exclude
    fun getIconId(): String

    var tags: List<String>

    var successFormula: String

    var resultFormula: String

    var dependencies: List<Dependency>

    var restrictions: List<Restriction>

    enum class INFO(val id: String) {
        STRIKE("strike") {
            override fun getIconRes(): Int {
                return R.drawable.ic_strength
            }
        },
        HOWL("howl") {
            override fun getIconRes(): Int {
                return R.drawable.ic_strength
            }
        };

        abstract fun getIconRes(): Int

        companion object {
            const val DEFAULT = "default"
            private val valuesMap: Map<String, INFO> = values().associateBy { it.id }

            fun isSupported(item: GameSkill): Boolean {
                return valuesMap[item.id] != null
            }

            fun getIconId(id: String): Int {
                return valuesMap[id]?.getIconRes() ?: R.drawable.ic_photo
            }
        }
    }
}