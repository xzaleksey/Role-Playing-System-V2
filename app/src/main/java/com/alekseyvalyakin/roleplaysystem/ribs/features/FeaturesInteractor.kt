package com.alekseyvalyakin.roleplaysystem.ribs.features

import com.alekseyvalyakin.roleplaysystem.data.firestore.features.FeaturesRepository
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import timber.log.Timber
import javax.inject.Inject

@RibInteractor
class FeaturesInteractor : BaseInteractor<FeaturesPresenter, FeaturesRouter>() {

    @Inject
    lateinit var presenter: FeaturesPresenter
    @Inject
    lateinit var analyticsReporter: AnalyticsReporter
    @Inject
    lateinit var featuresRepository: FeaturesRepository

    private val screenName = "Features"

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        analyticsReporter.setCurrentScreen(screenName)
        featuresRepository.observeSortedFeatures()
                .subscribeWithErrorLogging { features ->
                    Timber.d("Features list")
                    if (features.isNotEmpty()) {
                        featuresRepository.voteForFeature(features.first())
                                .subscribeWithErrorLogging()
                                .addToDisposables()
                    }
                }.addToDisposables()
    }

}
