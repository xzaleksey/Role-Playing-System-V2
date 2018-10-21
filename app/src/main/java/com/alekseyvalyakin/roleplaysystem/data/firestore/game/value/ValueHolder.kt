package com.alekseyvalyakin.roleplaysystem.data.firestore.game.value

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreIdModel
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils

interface ValueHolder : FireStoreIdModel {
    fun getType(): ValueType

    fun getValue(): Double
}

enum class ValueType {
    STAT,
    CLASS,
    SKILL
}

data class StatHolder(
        override var id: String = StringUtils.EMPTY_STRING,
        var value: Int
) : ValueHolder {

    override fun getType(): ValueType {
        return ValueType.STAT
    }

    override fun getValue(): Double {
        return value.toDouble()
    }
}