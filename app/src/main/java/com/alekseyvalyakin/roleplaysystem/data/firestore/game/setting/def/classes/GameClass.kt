package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes

import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreIdModel
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.Selectable
import com.google.firebase.firestore.Exclude


interface GameClass : FireStoreIdModel, Selectable {
    @Exclude
    fun getDisplayedName(): String

    @Exclude
    fun getDisplayedDescription(): String

    @Exclude
    fun getIconId(): String

    enum class INFO(val id: String) {
        FIGHTER("fighter") {
            override fun getIconRes(): Int {
                return R.drawable.ic_strength
            }
        },
        RANGER("ranger") {
            override fun getIconRes(): Int {
                return R.drawable.ic_dexterity
            }
        };

        abstract fun getIconRes(): Int

        companion object {
            const val DEFAULT = "default"

            fun isSupported(gameStat: GameClass): Boolean {
                return values().any { it.id == gameStat.id }
            }

            fun getIconId(id: String): Int {
                val iconRes = values().firstOrNull { it.id == id }?.getIconRes()
                if (iconRes != null) {
                    return iconRes
                }

                return R.drawable.ic_photo
            }
        }
    }
}