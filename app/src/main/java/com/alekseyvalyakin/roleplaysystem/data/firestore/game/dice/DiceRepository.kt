package com.alekseyvalyakin.roleplaysystem.data.firestore.game.dice

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDateCreate
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.BaseGameUserFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.GameUserFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.rxfirebase2.RxFirestore
import io.reactivex.Flowable
import io.reactivex.Single


class DicesRepositoryImpl(
        userRepository: UserRepository
) : BaseGameUserFireStoreRepository<FirebaseDiceCollection>(FirebaseDiceCollection::class.java, userRepository), DicesRepository {

    override fun createDocument(gameId: String, data: FirebaseDiceCollection): Single<FirebaseDiceCollection> {
        return RxFirestore.addDocumentHasId(getCollection(gameId, getUserId()), data)
    }

    override fun observeDiceCollectionsOrdered(gameId: String): Flowable<List<FirebaseDiceCollection>> {
        val query = getCollection(gameId, getUserId())
                .orderBy(HasDateCreate.FIELD_DATE_CREATE, Query.Direction.DESCENDING)
        return observeQueryCollection(query)
    }


    override fun getCollection(gameId: String, userId: String): CollectionReference {
        return FirestoreCollection.DICES(gameId, userId).getDbCollection()
    }

}

interface DicesRepository : GameUserFireStoreRepository<FirebaseDiceCollection> {
    fun observeDiceCollectionsOrdered(gameId: String): Flowable<List<FirebaseDiceCollection>>
}