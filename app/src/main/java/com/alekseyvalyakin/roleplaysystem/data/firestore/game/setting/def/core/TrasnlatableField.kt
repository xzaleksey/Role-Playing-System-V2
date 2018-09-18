package com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.core

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreModel
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude
import java.util.*

data class TrasnlatableField(
        val default: String = StringUtils.EMPTY_STRING,
        val ru: String = StringUtils.EMPTY_STRING
) : FireStoreModel {

    @Exclude
    fun getText(): String {
        if (Locale.getDefault().country.equals(RU, true)) {
            return ru
        }

        return default
    }

    companion object {
        const val DEFAULT = "default"
        const val RU = "ru"
    }
}