package com.alekseyvalyakin.roleplaysystem.data.firestore

import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

sealed class FirestoreCollection(
        private val root: FirestoreCollection? = null,
        val directory: String
) {
    object USERS : FirestoreCollection(directory = "users")
    object GAMES : FirestoreCollection(directory = "games")
    object NONE : FirestoreCollection(directory = "none")
    class USERS_IN_GAME(gameId: String) : FirestoreCollection(GAMES, directory = "$gameId/users")
    class GAMES_IN_USER(userId: String) : FirestoreCollection(USERS, directory = "$userId/games")

    private fun getFullPath(): String {
        return (root?.getFullPath()?.plus("/") ?: StringUtils.EMPTY_STRING) + directory
    }

    fun getDbCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(getFullPath())
    }
}