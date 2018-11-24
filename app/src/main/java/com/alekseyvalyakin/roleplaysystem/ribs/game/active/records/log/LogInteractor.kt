package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.log


import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.log.LogMessage
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.log.LogRepository
import com.alekseyvalyakin.roleplaysystem.data.sound.SoundRecordInteractor
import com.alekseyvalyakin.roleplaysystem.di.activity.ThreadConfig
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameEvent
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.RecordState
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.RecordsRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.SearchFilterInteractor
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.jakewharton.rxrelay2.Relay
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import io.reactivex.Observable
import io.reactivex.Scheduler
import timber.log.Timber
import javax.inject.Inject

@RibInteractor
class LogInteractor : BaseInteractor<LogPresenter, RecordsRouter>() {

    @Inject
    lateinit var presenter: LogPresenter
    @Inject
    lateinit var activeGameEventRelay: Relay<ActiveGameEvent>
    @Inject
    lateinit var logViewModelProvider: LogViewModelProvider
    @Inject
    lateinit var logRepository: LogRepository
    @field:[Inject ThreadConfig(ThreadConfig.TYPE.UI)]
    lateinit var uiScheduler: Scheduler
    @field:[Inject ThreadConfig(ThreadConfig.TYPE.IO)]
    lateinit var ioScheduler: Scheduler
    @Inject
    lateinit var soundRecordInteractor: SoundRecordInteractor
    @Inject
    lateinit var game: Game
    @Inject
    lateinit var searchFilterInteractor: SearchFilterInteractor
    @Inject
    lateinit var analyticsReporter: AnalyticsReporter
    private val screenName = "Records_log"

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        analyticsReporter.setCurrentScreen(screenName)

        logViewModelProvider.observeViewModel()
                .subscribeWithErrorLogging { presenter.update(it) }
                .addToDisposables()

        presenter.observeUiEvents()
                .flatMap {
                    return@flatMap handleUiEvent(it).onErrorReturn { t ->
                        Timber.e(t)
                    }
                }
                .subscribeWithErrorLogging()
                .addToDisposables()

        soundRecordInteractor.observeRecordingState()
                .observeOn(uiScheduler)
                .subscribeWithErrorLogging {
                    if (it.e != null) {
                        Timber.e(it.e)
                    } else if (it.isRecordingFinished()) {
                        Timber.d("Recording finished")
                    }
                    presenter.updateRecordState(RecordState(it))
                }

    }

    private fun handleUiEvent(uiEvent: LogPresenter.UiEvent): Observable<Any> {
        return when (uiEvent) {
            is LogPresenter.UiEvent.SendTextMessage -> {
                if (uiEvent.text.isNotBlank()) {
                    searchFilterInteractor.clearSearchInput()
                    return logRepository.createDocument(game.id, LogMessage.createTextModel(uiEvent.text))
                            .toObservable()
                            .cast(Any::class.java)
                }
                Observable.empty<Any>()
            }
            is LogPresenter.UiEvent.StartRecording -> {
                return Observable.fromCallable {
                    analyticsReporter.logEvent(GameLogAnalyticsEvent.StartRecord(game))
                    soundRecordInteractor.startRecordFile(game.id)
                }
            }

        }
    }
}


