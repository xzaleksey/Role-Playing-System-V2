package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.races

import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreIdModel
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.Selectable
import com.google.firebase.firestore.Exclude


interface GameRace : FireStoreIdModel, Selectable {
    @Exclude
    fun getDisplayedName(): String

    @Exclude
    fun getDisplayedDescription(): String

    @Exclude
    fun getIconId(): String

    enum class INFO(val id: String) {
        ELF("elf") {
            override fun getIconRes(): Int {
                return R.drawable.ic_strength
            }
        },
        HUMAN("human") {
            override fun getIconRes(): Int {
                return R.drawable.ic_dexterity
            }
        },
        HALF_ORC("half-orc") {
            override fun getIconRes(): Int {
                return R.drawable.ic_dexterity
            }
        },
        GNOME("gnome") {
            override fun getIconRes(): Int {
                return R.drawable.ic_dexterity
            }
        },
        DWARF("dwarf") {
            override fun getIconRes(): Int {
                return R.drawable.ic_dexterity
            }
        },
        TIEFLING("tiefling") {
            override fun getIconRes(): Int {
                return R.drawable.ic_dexterity
            }
        },
        DRAGONBORN("dragonborn") {
            override fun getIconRes(): Int {
                return R.drawable.ic_dexterity
            }
        },
        HALFLING("halfling") {
            override fun getIconRes(): Int {
                return R.drawable.ic_dexterity
            }
        };

        abstract fun getIconRes(): Int

        companion object {
            const val DEFAULT = "default"
            private val valuesMap: Map<String, INFO> = INFO.values().associateBy { it.id }

            fun isSupported(item: GameRace): Boolean {
                return valuesMap[item.id] != null
            }

            fun getIconId(id: String): Int {
                return valuesMap[id]?.getIconRes() ?: R.drawable.ic_photo
            }
        }
    }
}