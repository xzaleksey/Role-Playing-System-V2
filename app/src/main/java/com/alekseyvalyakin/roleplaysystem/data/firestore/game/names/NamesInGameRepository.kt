package com.alekseyvalyakin.roleplaysystem.data.firestore.game.names

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.BaseFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreRepository
import com.google.firebase.firestore.CollectionReference
import io.reactivex.Single
import java.util.*


class FirestoreNamesRepositoryIml : BaseFireStoreRepository<FirestoreNames>(FirestoreNames::class.java), FirestoreNamesRepository {

    override fun createEmptyDocument(): Single<FirestoreNames> {
        throw IllegalStateException("Can't create new name")
    }

    override fun getCollection(): CollectionReference {
        return FirestoreCollection.DefaultNames.getDbCollection()
    }

    override fun getNames(): Single<FirestoreNames> {
        return getDocumentSingle(getId())
    }

    fun getId(): String {
        if (Locale.getDefault().country.equals(RU, true)) {
            return RU
        }

        return DEFAULT
    }

    companion object {
        const val DEFAULT = "default"
        const val RU = "ru"
    }

}

interface FirestoreNamesRepository : FireStoreRepository<FirestoreNames> {
    fun getNames(): Single<FirestoreNames>
}