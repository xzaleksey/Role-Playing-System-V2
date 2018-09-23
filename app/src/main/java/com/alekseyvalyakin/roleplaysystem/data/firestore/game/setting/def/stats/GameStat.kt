package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats

import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreIdModel
import com.google.firebase.firestore.Exclude


interface GameStat : FireStoreIdModel {
    @Exclude
    fun getDisplayedName(): String

    @Exclude
    fun getDisplayedDescription(): String

    fun selected(): Boolean

    @Exclude
    fun getIconId(): String


    enum class INFO(val id: String) {
        STRENGTH("strength") {
            override fun getIconRes(): Int {
                return R.drawable.ic_strength
            }
        },
        CONSTITUTION("constitution") {
            override fun getIconRes(): Int {
                return R.drawable.ic_constitution
            }
        },
        DEXTERITY("dexterity") {
            override fun getIconRes(): Int {
                return R.drawable.ic_dexterity
            }
        },
        INTELLIGENCE("intelligence") {
            override fun getIconRes(): Int {
                return R.drawable.ic_intelligence
            }
        },
        WISDOM("wisdom") {
            override fun getIconRes(): Int {
                return R.drawable.ic_wisdom
            }
        },
        CHARISMA("charisma") {
            override fun getIconRes(): Int {
                return R.drawable.ic_charisma
            }
        };

        abstract fun getIconRes(): Int

        companion object {
            const val DEFAULT = "default"

            fun isSupported(gameStat: GameStat): Boolean {
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