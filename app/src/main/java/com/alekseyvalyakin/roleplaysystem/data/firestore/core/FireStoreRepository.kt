package com.alekseyvalyakin.roleplaysystem.data.firestore.core

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface FireStoreRepository<T : HasId> {
    fun getDocumentSingle(id: String): Single<T>
    fun observeDocument(id: String): Flowable<T>
    fun observeDocumentDelete(id: String): Completable
    fun observeCollection(): Flowable<List<T>>
    fun updateFieldOffline(id: String, value: Any, fieldName: String): Completable
    fun observeQueryCollection(query: Query): Flowable<List<T>>
    fun createEmptyDocument(): Single<T>
    fun getCollection(): CollectionReference
    fun deleteDocumentOffline(id: String): Completable
    fun createDocumentWithId(gameId: String, data: T): Single<T>
    fun createDocument(gameId: String, data: T): Single<T>
}