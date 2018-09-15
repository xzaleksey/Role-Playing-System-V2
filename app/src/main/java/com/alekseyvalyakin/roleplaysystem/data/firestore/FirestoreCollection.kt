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

    class UsersInGame(gameId: String) : FirestoreCollection(GAMES, directory = "$gameId/users")
    class PhotosInGame(gameId: String) : FirestoreCollection(GAMES, directory = "$gameId/photos")
    class GamesInUser(userId: String) : FirestoreCollection(USERS, directory = "$userId/games")

    class DICES(gameId: String, userId: String) :
            FirestoreCollection(UsersInGame(gameId),
                    directory = "$userId/dices/")

    private fun getFullPath(): String {
        return (root?.getFullPath()?.plus("/") ?: StringUtils.EMPTY_STRING) + directory
    }

    fun getDbCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(getFullPath())
    }
}