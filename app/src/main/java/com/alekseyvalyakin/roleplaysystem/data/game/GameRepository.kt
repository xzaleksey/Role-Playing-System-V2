package com.alekseyvalyakin.roleplaysystem.data.game

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.utils.setId
import com.google.firebase.firestore.DocumentSnapshot
import com.rxfirebase2.RxFirestore
import io.reactivex.Completable
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

    override fun observeAllActiveGames(): Flowable<List<Game>> {
        val query = gamesCollection()
                .whereEqualTo(Game.FIELD_STATUS, GameStatus.ACTIVE)
                .orderBy(Game.FIELD_DATE)
        return RxFirestore.observeQueryRefHasId(query, Game::class.java)
    }

    override fun saveName(id: String, text: String): Completable {
        return updateField(id, text, Game.FIELD_NAME)
    }

    override fun saveDescription(id: String, text: String): Completable {
        return updateField(id, text, Game.FIELD_DESCRIPTION)
    }

    override fun savePassword(id: String, text: String): Completable {
        return updateField(id, text, Game.FIELD_PASSWORD)
    }

    private fun updateField(id: String, text: String, fieldName: String): Completable {
        val document = gamesCollection().document(id)
        return RxFirestore.updateDocumentOffline(document, mapOf(fieldName to text))
    }

    private fun gamesCollection() = FirestoreCollection.GAMES.getDbCollection()
}

interface GameRepository {
    fun createDraftGame(): Single<Game>

    fun observeGame(id: String): Flowable<Game>

    fun saveName(id: String, text: String): Completable

    fun saveDescription(id: String, text: String): Completable

    fun savePassword(id: String, text: String): Completable

    fun observeAllActiveGames(): Flowable<List<Game>>
}