package com.alekseyvalyakin.roleplaysystem.ribs.license

import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

@RibInteractor
class LicenseInteractor : BaseInteractor<LicensePresenter, LicenseRouter>() {

    @Inject
    lateinit var presenter: LicensePresenter
    @Inject
    lateinit var analyticsReporter: AnalyticsReporter

    private val screenName = "License"

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        analyticsReporter.setCurrentScreen(screenName)
    }

}
