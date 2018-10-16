package com.alekseyvalyakin.roleplaysystem.data.firestore.user

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreIdModel
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude

/**
 * POJO
 */
data class User(
        var email: String = StringUtils.EMPTY_STRING,
        var photoUrl: String = StringUtils.EMPTY_STRING,
        var countOfGamesPlayed: Int = 0,
        var countOfGamesMastered: Int = 0,
        var displayName: String = StringUtils.EMPTY_STRING,
        var token: String = StringUtils.EMPTY_STRING,
        @Exclude
        @set:Exclude
        @get:Exclude
        override var id: String = StringUtils.EMPTY_STRING
) : FireStoreIdModel {

    companion object {
        const val FIELD_DISPLAY_NAME = "displayName"
        const val FIELD_PHOTO_URL = "photoUrl"

        val EMPTY_USER = User()
    }
}
