package com.alekseyvalyakin.roleplaysystem.data.firestore.features

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.BaseFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.FireStoreRepository
import com.google.firebase.firestore.CollectionReference
import io.reactivex.Single

class FeaturesRepositoryImpl : BaseFireStoreRepository<Feature>(Feature::class.java),FeaturesRepository {

    override fun createEmptyDocument(): Single<Feature> {
        throw IllegalAccessException("Forbidden for users")
    }

    override fun getCollection(): CollectionReference {
        return FirestoreCollection.FEATURES.getDbCollection()
    }

}

interface FeaturesRepository : FireStoreRepository<Feature> {
}