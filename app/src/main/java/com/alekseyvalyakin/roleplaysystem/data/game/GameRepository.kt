package com.alekseyvalyakin.roleplaysystem.data.game

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.BaseFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.data.game.gamesinuser.GamesInUserRepository
import com.alekseyvalyakin.roleplaysystem.data.game.useringame.UserInGameRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class GameRepositoryImpl(
        private val userRepository: UserRepository,
        private val userInGameRepository: UserInGameRepository,
        private val gamesInUserRepository: GamesInUserRepository
) : BaseFireStoreRepository<Game>(Game::class.java), GameRepository {

    private val instance = FirebaseFirestore.getInstance()

    override fun observeAllActiveGames(): Flowable<List<Game>> {
        val query = getCollection()
                .whereEqualTo(Game.FIELD_STATUS, GameStatus.ACTIVE.value)
                .orderBy(Game.FIELD_DATE, Query.Direction.DESCENDING)
        return observeQueryCollection(query)
    }

    override fun observeAllGamesDescending(): Flowable<List<Game>> {
        val query = getCollection().orderBy(Game.FIELD_DATE, Query.Direction.DESCENDING)
        return observeQueryCollection(query)
    }

    override fun saveName(id: String, text: String): Completable {
        return updateFieldOffline(id, text, Game.FIELD_NAME)
    }

    override fun saveDescription(id: String, text: String): Completable {
        return updateFieldOffline(id, text, Game.FIELD_DESCRIPTION)
    }

    override fun savePassword(id: String, text: String): Completable {
        return updateFieldOffline(id, text, Game.FIELD_PASSWORD)
    }

    override fun activateGame(id: String): Completable {
        return updateFieldOffline(id, GameStatus.ACTIVE.value, Game.FIELD_STATUS)
    }

    override fun createDocument(): Single<Game> {
        return userRepository.getCurrentUserSingle().flatMap { user ->
            val gameToCreate = Game(masterId = user.id, status = GameStatus.DRAFT.value, masterName = user.displayName)
            val writeBatch = instance.batch()
            val gameId = getCollection().document().id

            writeBatch.set(getDocumentReference(gameId), gameToCreate)
            userInGameRepository.addCurrentUserInGame(writeBatch, gameId)
            gamesInUserRepository.addGameInUser(writeBatch, gameId)
            writeBatch.commit()
            return@flatMap getDocumentSingle(gameId)
        }
    }

    override fun getCollection(): CollectionReference {
        return FirestoreCollection.GAMES.getDbCollection()
    }
}

interface GameRepository : FireStoreRepository<Game> {

    fun saveName(id: String, text: String): Completable

    fun saveDescription(id: String, text: String): Completable

    fun savePassword(id: String, text: String): Completable

    fun observeAllActiveGames(): Flowable<List<Game>>

    fun activateGame(id: String): Completable

    fun observeAllGamesDescending(): Flowable<List<Game>>
}