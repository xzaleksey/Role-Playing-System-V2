package com.alekseyvalyakin.roleplaysystem.data.firestore.core.game

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasId
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

interface GameFireStoreRepository<T : HasId> {
    fun getDocumentSingle(id: String, gameId: String): Single<T>
    fun observeDocument(id: String, gameId: String): Flowable<T>
    fun observeCollection(gameId: String): Flowable<List<T>>
    fun updateFieldOffline(id: String, value: Any, fieldName: String, gameId: String): Completable
    fun observeQueryCollection(query: Query, gameId: String): Flowable<List<T>>
    fun createDocument(gameId: String, data: T): Single<T>
    fun getCollection(gameId: String): CollectionReference
    fun deleteDocumentOffline(gameId: String, id: String): Completable
    fun createDocumentWithId(gameId: String, data: T): Single<T>
}