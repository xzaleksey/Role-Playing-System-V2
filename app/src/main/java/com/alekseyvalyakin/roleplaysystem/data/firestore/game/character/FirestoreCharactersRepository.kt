package com.alekseyvalyakin.roleplaysystem.data.firestore.game.character

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDateCreate
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.BaseGameFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.GameFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import io.reactivex.Flowable


class FirestoreCharactersRepositoryImpl(
        userRepository: UserRepository
) : BaseGameFireStoreRepository<FirestoreGameCharacter>(FirestoreGameCharacter::class.java), FirestoreCharactersRepository {

    override fun observeCharactersCollectionsOrdered(gameId: String): Flowable<List<FirestoreGameCharacter>> {
        val query = getCollection(gameId)
                .orderBy(HasDateCreate.FIELD_DATE_CREATE, Query.Direction.DESCENDING)
        return observeQueryCollection(query, gameId)
    }

    override fun getCollection(gameId: String): CollectionReference {
        return FirestoreCollection.CharactersInGame(gameId).getDbCollection()
    }
}

interface FirestoreCharactersRepository : GameFireStoreRepository<FirestoreGameCharacter> {
    fun observeCharactersCollectionsOrdered(gameId: String): Flowable<List<FirestoreGameCharacter>>
}