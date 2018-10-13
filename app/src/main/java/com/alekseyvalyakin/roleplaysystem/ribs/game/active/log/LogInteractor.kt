package com.alekseyvalyakin.roleplaysystem.ribs.game.active.log

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.log.LogMessage
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.log.LogRepository
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameEvent
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.jakewharton.rxrelay2.Relay
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

@RibInteractor
class LogInteractor : BaseInteractor<LogPresenter, LogRouter>() {

    @Inject
    lateinit var presenter: LogPresenter
    @Inject
    lateinit var activeGameEventRelay: Relay<ActiveGameEvent>
    @Inject
    lateinit var logViewModelProvider: LogViewModelProvider
    @Inject
    lateinit var logRepository: LogRepository
    @Inject
    lateinit var game: Game
    @Inject
    lateinit var analyticsReporter: AnalyticsReporter
    private val screenName = "MasterLog"

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        analyticsReporter.setCurrentScreen(screenName)

        logViewModelProvider.observeViewModel()
                .subscribeWithErrorLogging { presenter.update(it) }
                .addToDisposables()

        presenter.observeUiEvents()
                .flatMap(this::handleUiEvent)
                .onErrorReturn { Timber.e(it) }
                .subscribeWithErrorLogging()
                .addToDisposables()
    }

    private fun handleUiEvent(uiEvent: LogPresenter.UiEvent): Observable<*> {
        return when (uiEvent) {
            is LogPresenter.UiEvent.SendTextMessage -> {
                logRepository.createDocument(game.id, LogMessage.createTextModel(uiEvent.text))
                        .toObservable()
            }
        }
    }
}


