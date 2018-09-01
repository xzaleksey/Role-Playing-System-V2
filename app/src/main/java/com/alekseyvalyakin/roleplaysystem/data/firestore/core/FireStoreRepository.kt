package com.alekseyvalyakin.roleplaysystem.data.firestore.core

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface FireStoreRepository<T : HasId> {
    fun getDocumentSingle(id: String): Single<T>
    fun observeDocument(id: String): Flowable<T>
    fun observeCollection(): Flowable<List<T>>
    fun updateFieldOffline(id: String, value: Any, fieldName: String): Completable
    fun observeQueryCollection(query: Query): Flowable<List<T>>
    fun createDocument(): Single<T>
    fun getCollection(): CollectionReference
    fun deleteDocumentOffline(id: String): Completable
}