package com.alekseyvalyakin.roleplaysystem.data.game

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.data.game.useringame.UserInGameRepository
import com.alekseyvalyakin.roleplaysystem.utils.setId
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.rxfirebase2.RxFirestore
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class GameRepositoryImpl(
        private val userRepository: UserRepository,
        private val userInGameRepository: UserInGameRepository
) : GameRepository {

    override fun observeGame(id: String): Flowable<Game> {
        return RxFirestore.observeDocumentRef(gamesCollection().document(id), Game::class.java)
                .setId(id)
    }

    override fun createDraftGame(): Single<Game> {
        return userRepository.getCurrentUserSingle().flatMap { user ->
            val gameToCreate = Game(masterId = user.id, status = GameStatus.DRAFT.value, masterName = user.displayName)
            return@flatMap RxFirestore.addDocument(gamesCollection(), gameToCreate)
                    .map {
                        val createdGame = it.toObject(Game::class.java, DocumentSnapshot.ServerTimestampBehavior.ESTIMATE)
                        createdGame!!.id = it.id
                        return@map createdGame
                    }.flatMap { createdGame ->
                        userInGameRepository.createUserInGameInfo(createdGame.id).toSingle { createdGame }
                    }

        }
    }

    override fun observeAllActiveGames(): Flowable<List<Game>> {
        val query = gamesCollection()
                .whereEqualTo(Game.FIELD_STATUS, GameStatus.ACTIVE.value)
                .orderBy(Game.FIELD_DATE, Query.Direction.DESCENDING)
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

    override fun activateGame(id: String): Completable {
        return updateField(id, GameStatus.ACTIVE.value, Game.FIELD_STATUS)
    }

    private fun updateField(id: String, text: String, fieldName: String): Completable {
        val document = gamesCollection().document(id)
        return RxFirestore.updateDocumentOffline(document, mapOf(fieldName to text))
    }

    private fun updateField(id: String, value: Int, fieldName: String): Completable {
        val document = gamesCollection().document(id)
        return RxFirestore.updateDocumentOffline(document, mapOf(fieldName to value))
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

    fun activateGame(id: String): Completable
}