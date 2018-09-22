package com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.BaseGameFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.GameFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.observeCollectionByDateCreate
import com.google.firebase.firestore.CollectionReference
import io.reactivex.Completable
import io.reactivex.Flowable


class PhotoInGameRepositoryIml : BaseGameFireStoreRepository<FireStoreIdPhoto>(FireStoreIdPhoto::class.java), PhotoInGameRepository {

    override fun observeByDateCreate(gameId: String): Flowable<List<FireStoreIdPhoto>> {
        return observeCollectionByDateCreate(gameId)
    }

    override fun getCollection(gameId: String): CollectionReference {
        return FirestoreCollection.PhotosInGame(gameId).getDbCollection()
    }

    override fun switchVisibility(gameId: String, id: String, fireStoreVisibility: FireStoreVisibility): Completable {
        val updateMap = HashMap<Any, Any>()
        updateMap[FirestorePhotoState.VISIBILITY_STATE] = fireStoreVisibility.value
        return updateFieldOffline(id, updateMap, FireStoreIdPhoto.STATE_FIELD, gameId)
    }

}

interface PhotoInGameRepository : GameFireStoreRepository<FireStoreIdPhoto> {
    fun observeByDateCreate(gameId: String): Flowable<List<FireStoreIdPhoto>>

    fun switchVisibility(gameId: String, id: String, fireStoreVisibility: FireStoreVisibility): Completable
}