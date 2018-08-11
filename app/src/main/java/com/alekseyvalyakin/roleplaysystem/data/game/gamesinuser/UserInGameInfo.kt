package com.alekseyvalyakin.roleplaysystem.data.game.gamesinuser

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasId
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable
import java.util.*

data class GamesInUserInfo(
        @ServerTimestamp var dateUpdate: Date? = null,

        @Exclude
        @set:Exclude
        @get:Exclude
        @Volatile
        override var id: String = ""
) : HasId, Serializable {


    companion object {
        const val serialVersionUID = 1L

        val EMPTY_GAME_IN_USERS = GamesInUserInfo()
    }

}
