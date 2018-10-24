package com.alekseyvalyakin.roleplaysystem.data.firestore.game.item

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDateCreate
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.BaseGameFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.GameFireStoreRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import io.reactivex.Flowable


class FirestoreItemsRepositoryImpl : BaseGameFireStoreRepository<FireStoreItem>(FireStoreItem::class.java), FirestoreItemsRepository {

    override fun observeCharactersCollectionsOrdered(gameId: String): Flowable<List<FireStoreItem>> {
        val query = getCollection(gameId)
                .orderBy(HasDateCreate.FIELD_DATE_CREATE, Query.Direction.DESCENDING)
        return observeQueryCollection(query, gameId)
    }

    override fun getCollection(gameId: String): CollectionReference {
        return FirestoreCollection.ItemsInGame(gameId).getDbCollection()
    }
}

interface FirestoreItemsRepository : GameFireStoreRepository<FireStoreItem> {
    fun observeCharactersCollectionsOrdered(gameId: String): Flowable<List<FireStoreItem>>
}