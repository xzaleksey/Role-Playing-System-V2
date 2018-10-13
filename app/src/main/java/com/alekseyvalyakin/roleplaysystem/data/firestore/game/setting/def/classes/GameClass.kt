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
                return R.drawable.ic_fighter
            }
        },
        RANGER("ranger") {
            override fun getIconRes(): Int {
                return R.drawable.ic_ranger
            }
        },
        BARD("bard") {
            override fun getIconRes(): Int {
                return R.drawable.ic_bard
            }
        },
        CLERIC("cleric") {
            override fun getIconRes(): Int {
                return R.drawable.ic_cleric
            }
        },
        DRUID("druid") {
            override fun getIconRes(): Int {
                return R.drawable.ic_druid
            }
        },
        PALADIN("paladin") {
            override fun getIconRes(): Int {
                return R.drawable.ic_paladin
            }
        },
        BARBARIAN("barbarian") {
            override fun getIconRes(): Int {
                return R.drawable.ic_barbarian
            }
        },
        ROGUE("rogue") {
            override fun getIconRes(): Int {
                return R.drawable.ic_rougue
            }
        },
        SORCERER("sorcerer") {
            override fun getIconRes(): Int {
                return R.drawable.ic_sorcerer
            }
        },
        WARLOCK("warlock") {
            override fun getIconRes(): Int {
                return R.drawable.ic_warlock
            }
        },
        WIZARD("wizard") {
            override fun getIconRes(): Int {
                return R.drawable.ic_wizard
            }
        },
        MONK("monk") {
            override fun getIconRes(): Int {
                return R.drawable.ic_monk
            }
        };

        abstract fun getIconRes(): Int

        companion object {
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