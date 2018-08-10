package com.alekseyvalyakin.roleplaysystem.data.game.useringame

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.rxfirebase2.RxFirestore
import io.reactivex.Completable


class UserInGameRepositoryImpl(
        private val userRepository: UserRepository
) : UserInGameRepository {

    override fun createUserInGameInfo(gameId: String): Completable {
        val currentUser = userRepository.getCurrentUser()
                ?: throw RuntimeException("No user")
        val document = userInGameCollection(gameId).document(currentUser.uid)

        return RxFirestore.setDocument(document, UserInGameInfo())
    }

    private fun userInGameCollection(gameId: String) = FirestoreCollection.USERS_IN_GAME(gameId).getDbCollection()
}

interface UserInGameRepository {
    fun createUserInGameInfo(gameId: String): Completable
}