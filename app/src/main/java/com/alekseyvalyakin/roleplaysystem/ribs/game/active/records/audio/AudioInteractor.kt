package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio


import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.RecordsRouter
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

@RibInteractor
class AudioInteractor : BaseInteractor<AudioPresenter, RecordsRouter>() {

    @Inject
    lateinit var presenter: AudioPresenter
    @Inject
    lateinit var analyticsReporter: AnalyticsReporter
    @Inject
    lateinit var audioViewModelProvider: AudioViewModelProvider
    private val screenName = "Records_audio"

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        analyticsReporter.setCurrentScreen(screenName)
        audioViewModelProvider.observeViewModel()
                .subscribeWithErrorLogging {  }
                .addToDisposables()
    }

}


