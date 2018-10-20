package com.alekseyvalyakin.roleplaysystem.data.firestore.game.gamesinuser

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreIdModel
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.ServerTimestamp
import java.io.Serializable
import java.util.*

data class GamesInUserInfo(
        @ServerTimestamp var dateUpdate: Date = Date(),

        @Exclude
        @set:Exclude
        @get:Exclude
        @Volatile
        override var id: String = StringUtils.EMPTY_STRING
) : FireStoreIdModel, Serializable {


    companion object {
        const val serialVersionUID = 1L

        val EMPTY_GAME_IN_USERS = GamesInUserInfo()
    }

}
