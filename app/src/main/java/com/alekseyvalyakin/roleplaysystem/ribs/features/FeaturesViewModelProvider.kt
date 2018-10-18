package com.alekseyvalyakin.roleplaysystem.ribs.features

import com.alekseyvalyakin.roleplaysystem.data.firestore.features.FeaturesRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.ribs.features.adapter.FeatureFlexibleViewModel
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.Flowable

class FeaturesViewModelProviderImpl(
        private val featuresRepository: FeaturesRepository,
        private val userRepository: UserRepository
) : FeaturesViewModelProvider {

    override fun subscribe(): Flowable<FeaturesModel> {
        return featuresRepository.observeSortedFeatures()
                .map { features ->
                    val currentUserInfo = userRepository.getCurrentUserInfo()
                            ?: return@map FeaturesModel(emptyList())
                    var canVote = true
                    val result = mutableListOf<IFlexible<*>>()
                    val canVoteLambda = { canVote }

                    for (feature in features) {
                        var votedByMe = false
                        if (feature.votes.contains(currentUserInfo.uid)) {
                            canVote = false
                            votedByMe = true
                        }
                        result.add(FeatureFlexibleViewModel(feature, canVoteLambda, votedByMe))
                    }
                    FeaturesModel(result)
                }
    }
}

interface FeaturesViewModelProvider {
    fun subscribe(): Flowable<FeaturesModel>
}