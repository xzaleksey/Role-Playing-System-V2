package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes

import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreIdModel
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.Selectable
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats.GameStat
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
        },
        BARD("bard") {
            override fun getIconRes(): Int {
                return R.drawable.ic_wisdom
            }
        },
        CLERIC("cleric") {
            override fun getIconRes(): Int {
                return R.drawable.ic_will
            }
        },
        DRUID("druid") {
            override fun getIconRes(): Int {
                return R.drawable.ic_perception
            }
        },
        PALADIN("paladin") {
            override fun getIconRes(): Int {
                return R.drawable.ic_constitution
            }
        },
        BARBARIAN("barbarian") {
            override fun getIconRes(): Int {
                return R.drawable.ic_constitution
            }
        },
        ROGUE("rogue") {
            override fun getIconRes(): Int {
                return R.drawable.ic_dexterity
            }
        },
        SORCERER("sorcerer") {
            override fun getIconRes(): Int {
                return R.drawable.ic_wisdom
            }
        },
        WARLOCK("warlock") {
            override fun getIconRes(): Int {
                return R.drawable.ic_wisdom
            }
        },
        WIZARD("wizard") {
            override fun getIconRes(): Int {
                return R.drawable.ic_intelligence
            }
        },
        MONK("monk") {
            override fun getIconRes(): Int {
                return R.drawable.ic_will
            }
        };

        abstract fun getIconRes(): Int

        companion object {
            const val DEFAULT = "default"
            private val valuesMap: Map<String, INFO> = INFO.values().associateBy { it.id }

            fun isSupported(item: GameClass): Boolean {
                return valuesMap[item.id] != null
            }

            fun getIconId(id: String): Int {
                return valuesMap[id]?.getIconRes() ?: R.drawable.ic_photo
            }
        }
    }
}