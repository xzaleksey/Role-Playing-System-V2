package com.alekseyvalyakin.roleplaysystem.data.firestore.core.game

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasDateCreate
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasId
import com.google.firebase.firestore.Query
import com.rxfirebase2.RxFirestore
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

abstract class BaseGameFireStoreRepository<T : HasId>(
        protected val clazz: Class<T>
) : GameFireStoreRepository<T> {

    override fun getDocumentSingle(id: String, gameId: String): Single<T> {
        return RxFirestore.getDocumentSingleHasId(getDocumentReference(id, gameId), clazz)
    }

    override fun observeDocument(id: String, gameId: String): Flowable<T> {
        return RxFirestore.observeDocumentRefHasId(getDocumentReference(id, gameId), clazz)
    }

    override fun observeCollection(gameId: String): Flowable<List<T>> {
        return RxFirestore.observeQueryRefHasId(getCollection(gameId), clazz)
    }

    override fun updateFieldOffline(id: String, value: Any, fieldName: String, gameId: String): Completable {
        return RxFirestore.updateDocumentOffline(getDocumentReference(id, gameId), mapOf(fieldName to value))
    }

    override fun observeQueryCollection(query: Query, gameId: String): Flowable<List<T>> {
        return RxFirestore.observeQueryRefHasId(query, clazz)
    }

    override fun deleteDocumentOffline(gameId: String, id: String): Completable {
        return RxFirestore.deleteDocumentOffline(getDocumentReference(id, gameId))
    }

    override fun createDocument(gameId: String, data: T): Single<T> {
        return RxFirestore.addDocumentHasId(getCollection(gameId), data)
    }

    override fun setDocumentWithId(gameId: String, data: T): Single<T> {
        return RxFirestore.setDocumentOffline(getCollection(gameId).document(data.id), data)
                .toSingleDefault(data)
    }

    protected fun getDocumentReference(id: String, gameId: String) = getCollection(gameId).document(id)
}

fun <T> BaseGameFireStoreRepository<T>.observeCollectionByDateCreate(gameId: String): Flowable<List<T>> where T : HasId,
                                                                                                              T : HasDateCreate {
    val query = getCollection(gameId)
            .orderBy(HasDateCreate.FIELD_DATE_CREATE, Query.Direction.DESCENDING)
    return observeQueryCollection(query, gameId)
}