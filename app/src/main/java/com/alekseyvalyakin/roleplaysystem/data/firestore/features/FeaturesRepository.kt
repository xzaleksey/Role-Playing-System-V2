package com.alekseyvalyakin.roleplaysystem.data.firestore.features

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.BaseFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.google.firebase.firestore.CollectionReference
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class FeaturesRepositoryImpl(
        val user: UserRepository
) : BaseFireStoreRepository<Feature>(Feature::class.java), FeaturesRepository {
    override fun observeSortedFeatures(): Flowable<List<Feature>> {
        return Flowable.empty()
    }

    override fun voteForFeature(feature: Feature): Completable {
        return Completable.complete()
    }

    override fun createEmptyDocument(): Single<Feature> {
        throw IllegalAccessException("Forbidden for users")
    }

    override fun getCollection(): CollectionReference {
        return FirestoreCollection.FEATURES.getDbCollection()
    }
}

interface FeaturesRepository {
    fun observeSortedFeatures(): Flowable<List<Feature>>

    fun voteForFeature(feature: Feature): Completable
}