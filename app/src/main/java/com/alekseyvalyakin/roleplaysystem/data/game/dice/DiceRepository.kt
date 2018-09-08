package com.alekseyvalyakin.roleplaysystem.data.game.dice

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.BaseGameUserFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.game.GameUserFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.google.firebase.firestore.CollectionReference
import com.rxfirebase2.RxFirestore
import io.reactivex.Single


class DicesRepositoryImpl(
        userRepository: UserRepository
) : BaseGameUserFireStoreRepository<FirebaseDiceCollection>(FirebaseDiceCollection::class.java, userRepository), DicesRepository {

    override fun createDocument(gameId: String, data: FirebaseDiceCollection): Single<FirebaseDiceCollection> {
        return RxFirestore.addDocument(getCollection(gameId, getUserId()), data)
                .map {
                    data.id = it.id
                    data
                }
    }

    override fun getCollection(gameId: String, userId: String): CollectionReference {
        return FirestoreCollection.DICES(gameId, userId).getDbCollection()
    }

}

interface DicesRepository : GameUserFireStoreRepository<FirebaseDiceCollection>