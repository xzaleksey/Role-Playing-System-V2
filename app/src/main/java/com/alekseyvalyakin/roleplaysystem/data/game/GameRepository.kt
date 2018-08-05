package com.alekseyvalyakin.roleplaysystem.data.game

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.google.firebase.firestore.DocumentSnapshot
import com.rxfirebase2.RxFirestore
import io.reactivex.Single

class GameRepositoryImpl(
        private val userRepository: UserRepository) : GameRepository {

    override fun createDraftGame(): Single<Game> {
        return userRepository.getCurrentUserSingle().flatMap { user ->
            val game = Game(masterId = user.id, status = GameStatus.DRAFT.value, masterName = user.displayName)
            return@flatMap RxFirestore.addDocument(FirestoreCollection.GAMES.getDbCollection(), game)
                    .map { it.toObject(Game::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE) }
        }
    }
}

interface GameRepository {
    fun createDraftGame(): Single<Game>
}