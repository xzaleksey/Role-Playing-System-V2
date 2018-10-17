package com.alekseyvalyakin.roleplaysystem.ribs.features

import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

@RibInteractor
class FeaturesInteractor : BaseInteractor<FeaturesPresenter, FeaturesRouter>() {

    @Inject
    lateinit var presenter: FeaturesPresenter
    @Inject
    lateinit var analyticsReporter: AnalyticsReporter

    private val screenName = "Features"

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        analyticsReporter.setCurrentScreen(screenName)

    }

}
