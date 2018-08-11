package com.alekseyvalyakin.roleplaysystem.data.game.gamesinuser

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.WriteBatch
import com.rxfirebase2.RxFirestore
import io.reactivex.Completable


class GamesInUserRepositoryImpl(
        private val userRepository: UserRepository
) : GamesInUserRepository {

    override fun createGamesInUserInfo(gameId: String): Completable {
        val document = createDocumentReference(gameId)
        return RxFirestore.setDocument(document, GamesInUserInfo())
    }

    override fun addGameInUser(writeBatch: WriteBatch, id: String) {
        writeBatch.set(createDocumentReference(id), GamesInUserInfo())
    }

    private fun createDocumentReference(gameId: String) = gamesInUserCollection().document(gameId)

    private fun currentUser() = (userRepository.getCurrentUser()
            ?: throw RuntimeException("No user"))

    override fun gamesInUserCollection() = FirestoreCollection.GAMES_IN_USER(currentUser().uid).getDbCollection()
}

interface GamesInUserRepository {
    fun createGamesInUserInfo(gameId: String): Completable

    fun gamesInUserCollection(): CollectionReference

    fun addGameInUser(writeBatch: WriteBatch, id: String)
}