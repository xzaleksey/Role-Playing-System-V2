package com.alekseyvalyakin.roleplaysystem.data.game

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.utils.setId
import com.google.firebase.firestore.DocumentSnapshot
import com.rxfirebase2.RxFirestore
import io.reactivex.Flowable
import io.reactivex.Single

class GameRepositoryImpl(
        private val userRepository: UserRepository) : GameRepository {

    override fun observeGame(id: String): Flowable<Game> {
        return RxFirestore.observeDocumentRef(gamesCollection().document(id), Game::class.java)
                .setId(id)
    }

    override fun createDraftGame(): Single<Game> {
        return userRepository.getCurrentUserSingle().flatMap { user ->
            val game = Game(masterId = user.id, status = GameStatus.DRAFT.value, masterName = user.displayName)
            return@flatMap RxFirestore.addDocument(gamesCollection(), game)
                    .map {
                        val createdGame = it.toObject(Game::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                        createdGame!!.id = it.id
                        return@map createdGame
                    }

        }
    }

    private fun gamesCollection() = FirestoreCollection.GAMES.getDbCollection()
}

interface GameRepository {
    fun createDraftGame(): Single<Game>

    fun observeGame(id: String): Flowable<Game>
}