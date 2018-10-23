package com.alekseyvalyakin.roleplaysystem.data.firestore.game.dice

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDateCreate
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.BaseGameUserFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.GameUserFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import io.reactivex.Flowable


class DicesRepositoryImpl(
        userRepository: UserRepository
) : BaseGameUserFireStoreRepository<FirestoreDiceCollection>(FirestoreDiceCollection::class.java, userRepository), DicesRepository {

    override fun observeDiceCollectionsOrdered(gameId: String): Flowable<List<FirestoreDiceCollection>> {
        val query = getCollection(gameId, getUserId())
                .orderBy(HasDateCreate.FIELD_DATE_CREATE, Query.Direction.DESCENDING)
        return observeQueryCollection(query)
    }


    override fun getCollection(gameId: String, userId: String): CollectionReference {
        return FirestoreCollection.DICES(gameId, userId).getDbCollection()
    }

}

interface DicesRepository : GameUserFireStoreRepository<FirestoreDiceCollection> {
    fun observeDiceCollectionsOrdered(gameId: String): Flowable<List<FirestoreDiceCollection>>
}