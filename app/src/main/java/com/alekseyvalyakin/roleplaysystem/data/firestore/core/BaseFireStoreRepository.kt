package com.alekseyvalyakin.roleplaysystem.data.firestore.core

import com.google.firebase.firestore.Query
import com.rxfirebase2.RxFirestore
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

abstract class BaseFireStoreRepository<T : HasId>(
        protected val clazz: Class<T>
) : FireStoreRepository<T> {

    override fun getDocumentSingle(id: String): Single<T> {
        return RxFirestore.getDocumentSingleHasId(getDocumentReference(id), clazz)
    }

    override fun observeDocument(id: String): Flowable<T> {
        return RxFirestore.observeDocumentRefHasId(getDocumentReference(id), clazz)
    }

    override fun observeCollection(): Flowable<List<T>> {
        return RxFirestore.observeQueryRefHasId(getCollection(), clazz)
    }

    override fun updateFieldOffline(id: String, value: Any, fieldName: String): Completable {
        return RxFirestore.updateDocumentOffline(getDocumentReference(id), mapOf(fieldName to value))
    }

    override fun observeQueryCollection(query: Query): Flowable<List<T>> {
        return RxFirestore.observeQueryRefHasId(query, clazz)
    }

    protected fun getDocumentReference(id: String) = getCollection().document(id)
}