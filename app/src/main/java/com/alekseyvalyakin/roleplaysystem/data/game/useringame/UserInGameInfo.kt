package com.alekseyvalyakin.roleplaysystem.data.game.useringame

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable
import java.util.*

data class UserInGameInfo(
        @ServerTimestamp var dateUpdate: Date? = null,

        @Exclude
        @set:Exclude
        @get:Exclude
        @Volatile
        override var id: String = ""
) : HasId, Serializable {


    companion object {
        const val serialVersionUID = 1L

        const val FIELD_DATE = "dateUpdate"

        val EMPTY_USER_IN_GAME_INFO = UserInGameInfo()
    }

}
