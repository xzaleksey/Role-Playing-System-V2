package com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.BaseGameFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.GameFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.observeCollectionByDateCreate
import com.google.firebase.firestore.CollectionReference
import com.rxfirebase2.RxFirestore
import io.reactivex.Flowable
import io.reactivex.Single


class PhotoIngameRepositoryIml : BaseGameFireStoreRepository<FireStorePhoto>(FireStorePhoto::class.java), PhotoInGameRepository {

    override fun createDocument(gameId: String, data: FireStorePhoto): Single<FireStorePhoto> {
        return RxFirestore.addDocumentHasId(getCollection(gameId), data)
    }

    override fun observeByDateCreate(gameId: String): Flowable<List<FireStorePhoto>> {
        return observeCollectionByDateCreate(gameId)
    }

    override fun getCollection(gameId: String): CollectionReference {
        return FirestoreCollection.PhotosInGame(gameId).getDbCollection()
    }
}

interface PhotoInGameRepository : GameFireStoreRepository<FireStorePhoto> {
    fun observeByDateCreate(gameId: String): Flowable<List<FireStorePhoto>>
}