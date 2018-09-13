package com.alekseyvalyakin.roleplaysystem.data.firestore.game.gamesinuser

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.WriteBatch
import com.rxfirebase2.RxFirestore
import io.reactivex.Completable
import io.reactivex.Flowable


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

    private fun createDocumentReference(gameId: String) = gamesInUserCollection(currentUser().uid).document(gameId)

    private fun currentUser() = (userRepository.getCurrentUserInfo()
            ?: throw RuntimeException("No user"))

    override fun gamesInUserCollection(id: String) = FirestoreCollection.GamesInUser(id).getDbCollection()

    override fun observeCurrentUserGames(): Flowable<List<GamesInUserInfo>> {
        val gamesInUserCollection = gamesInUserCollection(currentUser().uid)
        return RxFirestore.observeQueryRefHasId(gamesInUserCollection, GamesInUserInfo::class.java)
    }

    override fun observeUserGames(uid: String): Flowable<List<GamesInUserInfo>> {
        val gamesInUserCollection = gamesInUserCollection(uid)
        return RxFirestore.observeQueryRefHasId(gamesInUserCollection, GamesInUserInfo::class.java)
    }
}

interface GamesInUserRepository {
    fun createGamesInUserInfo(gameId: String): Completable

    fun gamesInUserCollection(id: String): CollectionReference

    fun addGameInUser(writeBatch: WriteBatch, id: String)

    fun observeCurrentUserGames(): Flowable<List<GamesInUserInfo>>

    fun observeUserGames(uid: String): Flowable<List<GamesInUserInfo>>
}