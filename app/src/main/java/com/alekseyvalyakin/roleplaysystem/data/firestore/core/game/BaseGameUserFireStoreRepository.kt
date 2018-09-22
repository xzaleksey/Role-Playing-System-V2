package com.alekseyvalyakin.roleplaysystem.data.firestore.core.game

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasId
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.google.firebase.firestore.Query
import com.rxfirebase2.RxFirestore
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

abstract class BaseGameUserFireStoreRepository<T : HasId>(
        protected val clazz: Class<T>,
        protected val userRepository: UserRepository
) : GameUserFireStoreRepository<T> {

    override fun getDocumentSingle(id: String, gameId: String): Single<T> {
        return RxFirestore.getDocumentSingleHasId(getDocumentReference(id, gameId), clazz)
    }

    override fun observeDocument(id: String, gameId: String): Flowable<T> {
        return RxFirestore.observeDocumentRefHasId(getDocumentReference(id, gameId), clazz)
    }

    override fun observeCollection(gameId: String): Flowable<List<T>> {
        return RxFirestore.observeQueryRefHasId(getCollection(gameId, getUserId()), clazz)
    }

    override fun updateFieldOffline(id: String, value: Any, fieldName: String, gameId: String): Completable {
        return RxFirestore.updateDocumentOffline(getDocumentReference(id, gameId), mapOf(fieldName to value))
    }

    override fun observeQueryCollection(query: Query): Flowable<List<T>> {
        return RxFirestore.observeQueryRefHasId(query, clazz)
    }

    override fun deleteDocument(id: String, gameId: String): Completable {
        return RxFirestore.deleteDocument(getDocumentReference(id, gameId))
    }

    override fun createDocument(gameId: String, data: T): Single<T> {
        return RxFirestore.addDocumentHasId(getCollection(gameId, getUserId()), data)
    }

    override fun createDocumentWithId(gameId: String, data: T): Single<T> {
        return RxFirestore.setDocumentOffline(getCollection(gameId, getUserId()).document(data.id), data)
                .toSingleDefault(data)
    }

    protected fun getDocumentReference(id: String, gameId: String) = getCollection(gameId, getUserId())
            .document(id)

    protected fun getUserId() = userRepository.getCurrentUserInfo()!!.uid
}