package com.alekseyvalyakin.roleplaysystem.data.firestore.user

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasId
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude

/**
 * POJO
 */
data class User(
        var email: String = StringUtils.EMPTY_STRING,
        var photoUrl: String? = null,
        var countOfGamesPlayed: Int = 0,
        var countOfGamesMastered: Int = 0,
        var displayName: String = StringUtils.EMPTY_STRING,
        var token: String = StringUtils.EMPTY_STRING,
        @Exclude
        @set:Exclude
        @get:Exclude
        override var id: String = StringUtils.EMPTY_STRING
) : HasId {

    companion object {
        const val FIELD_DISPLAY_NAME="displayName"

        val EMPTY_USER = User()
    }
}
