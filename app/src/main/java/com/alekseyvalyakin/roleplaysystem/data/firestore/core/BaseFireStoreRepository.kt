package com.alekseyvalyakin.roleplaysystem.data.firestore.core

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.rxfirebase2.RxFirestore
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

abstract class BaseFireStoreRepository<T : HasId>(
        protected val clazz: Class<T>
) : FireStoreRepository<T> {
    protected val firestoreInstance = FirebaseFirestore.getInstance()

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

    override fun deleteDocumentOffline(id: String): Completable {
        return RxFirestore.deleteDocumentOffline(getDocumentReference(id))
    }

    override fun createDocument(gameId: String, data: T): Single<T> {
        return RxFirestore.addDocumentHasId(getCollection(), data)
    }

    override fun observeDocumentDelete(id: String): Completable {
        return RxFirestore.observeDocumentDeleteRef(getDocumentReference(id))
    }

    override fun createDocumentWithId(gameId: String, data: T): Single<T> {
        return RxFirestore.setDocumentOffline(getCollection().document(data.id), data)
                .toSingleDefault(data)
    }

    protected fun getDocumentReference(id: String) = getCollection().document(id)
}