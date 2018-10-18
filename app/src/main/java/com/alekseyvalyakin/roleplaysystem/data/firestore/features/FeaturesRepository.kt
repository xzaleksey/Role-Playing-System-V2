package com.alekseyvalyakin.roleplaysystem.data.firestore.features

import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.BaseFireStoreRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Transaction
import com.rxfirebase2.RxFirestore
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class FeaturesRepositoryImpl(
        val user: UserRepository
) : BaseFireStoreRepository<Feature>(Feature::class.java), FeaturesRepository {

    override fun observeSortedFeatures(): Flowable<List<Feature>> {
        return observeCollection()
                .map {
                    return@map it.sortedByDescending {feature->
                        feature.votes.size
                    }
                }
    }

    override fun voteForFeature(feature: Feature): Completable {
        val featureReference = getDocumentReference(feature.id)
        return RxFirestore.runTransaction(firestoreInstance, Transaction.Function { transaction ->
            val documentSnapshot = transaction.get(featureReference)
            val currentUserInfo = user.getCurrentUserInfo()
            if (documentSnapshot.exists() && currentUserInfo != null) {
                transaction.update(featureReference, Feature.FIELD_VOTES, FieldValue.arrayUnion(currentUserInfo.uid))
            }
        })
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