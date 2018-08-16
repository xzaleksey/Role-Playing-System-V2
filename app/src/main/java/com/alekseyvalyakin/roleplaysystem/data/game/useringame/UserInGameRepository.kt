package com.alekseyvalyakin.roleplaysystem.data.game.useringame

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.WriteBatch
import com.rxfirebase2.RxFirestore
import io.reactivex.Completable


class UserInGameRepositoryImpl(
        private val userRepository: UserRepository
) : UserInGameRepository {

    override fun createUserInGameInfo(gameId: String): Completable {
        val document = createDocumentReference(gameId)
        return RxFirestore.setDocument(document, getNewUserInGameInfo())
    }

    override fun addCurrentUserInGame(writeBatch: WriteBatch, id: String) {
        writeBatch.set(createDocumentReference(id), getNewUserInGameInfo())
    }

    private fun createDocumentReference(gameId: String): DocumentReference {
        val currentUser = userRepository.getCurrentUserInfo()
                ?: throw RuntimeException("No user")
        return userInGameCollection(gameId).document(currentUser.uid)
    }

    private fun getNewUserInGameInfo() = UserInGameInfo()


    override fun userInGameCollection(gameId: String) = FirestoreCollection.USERS_IN_GAME(gameId).getDbCollection()
}

interface UserInGameRepository {
    fun createUserInGameInfo(gameId: String): Completable

    fun userInGameCollection(gameId: String): CollectionReference

    fun addCurrentUserInGame(writeBatch: WriteBatch, id: String)
}