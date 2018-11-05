package com.alekseyvalyakin.roleplaysystem.data.firestore

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.GameSetting
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

sealed class FirestoreCollection(
        private val root: FirestoreCollection? = null,
        val directory: String
) {
    object USERS : FirestoreCollection(directory = "users")
    object GAMES : FirestoreCollection(directory = "games")
    object SETTINGS : FirestoreCollection(directory = "settings")
    object FEATURES : FirestoreCollection(directory = "features")
    object NONE : FirestoreCollection(directory = "none")

    class UsersInGame(gameId: String) : FirestoreCollection(GAMES, directory = "$gameId/users")
    class PhotosInGame(gameId: String) : FirestoreCollection(GAMES, directory = "$gameId/photos")
    class StatsInGame(gameId: String) : FirestoreCollection(GAMES, directory = "$gameId/stats")
    class ItemsInGame(gameId: String) : FirestoreCollection(GAMES, directory = "$gameId/items")
    class ClassesInGame(gameId: String) : FirestoreCollection(GAMES, directory = "$gameId/classes")
    class SkillsInGame(gameId: String) : FirestoreCollection(GAMES, directory = "$gameId/skills")
    class RacesInGame(gameId: String) : FirestoreCollection(GAMES, directory = "$gameId/races")
    class TagsInGame(gameId: String) : FirestoreCollection(GAMES, directory = "$gameId/tags")
    class LogsInGame(gameId: String) : FirestoreCollection(GAMES, directory = "$gameId/logs")
    class CharactersInGame(gameId: String) : FirestoreCollection(GAMES, directory = "$gameId/characters")
    class GamesInUser(userId: String) : FirestoreCollection(USERS, directory = "$userId/games")

    class DICES(gameId: String, userId: String) :
            FirestoreCollection(UsersInGame(gameId),
                    directory = "$userId/dices/")


    object DefaultStats : FirestoreCollection(SETTINGS, "${GameSetting.DEFAULT.title}/stats")
    object DefaultClasses : FirestoreCollection(SETTINGS, "${GameSetting.DEFAULT.title}/classes")
    object DefaultRaces : FirestoreCollection(SETTINGS, "${GameSetting.DEFAULT.title}/races")
    object DefaultNames : FirestoreCollection(SETTINGS, directory = "${GameSetting.DEFAULT.title}/names")

    private fun getFullPath(): String {
        return (root?.getFullPath()?.plus("/") ?: StringUtils.EMPTY_STRING) + directory
    }

    fun getDbCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(getFullPath())
    }
}