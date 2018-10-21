package com.alekseyvalyakin.roleplaysystem.data.firestore.core

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.value.ValueHolder
import com.google.firebase.firestore.Exclude

interface HasLevel : ValueHolder {
    var level: Int

    @Exclude
    override fun getValue(): Double = level.toDouble()

    companion object {
        const val FIELD_LEVEL = "level"
    }
}