package com.alekseyvalyakin.roleplaysystem.ribs.game.active.log

import com.alekseyvalyakin.roleplaysystem.base.filter.FilterModel
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.log.LogMessage
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.log.LogRepository
import com.alekseyvalyakin.roleplaysystem.data.sound.SoundRecordInteractor
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameEvent
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import io.reactivex.BackpressureStrategy
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
    lateinit var soundRecordInteractor: SoundRecordInteractor
    @Inject
    lateinit var analyticsReporter: AnalyticsReporter
    private val screenName = "MasterLog"
    private val filterRelay = BehaviorRelay.createDefault<FilterModel>(FilterModel())

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        analyticsReporter.setCurrentScreen(screenName)

        logViewModelProvider.observeViewModel(filterRelay.toFlowable(BackpressureStrategy.LATEST).distinctUntilChanged())
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
                if (uiEvent.text.isNotBlank()) {
                    presenter.clearSearchInput()
                    return logRepository.createDocument(game.id, LogMessage.createTextModel(uiEvent.text))
                            .toObservable()
                }
                Observable.empty<Any>()
            }
            is LogPresenter.UiEvent.SearchInput -> {
                return Observable.fromCallable {
                    val value = filterRelay.value
                    filterRelay.accept(value.copy(previousQuery = value.query, query = uiEvent.text))
                }
            }
            is LogPresenter.UiEvent.OpenAudio -> {
                return Observable.fromCallable {
                    soundRecordInteractor.startRecordFile()
                }
            }
            is LogPresenter.UiEvent.OpenTexts -> {
                return Observable.fromCallable {
                    soundRecordInteractor.stopRecordingFile()
                }
            }
            is LogPresenter.UiEvent.StartRecording -> {
                return Observable.fromCallable {
                    soundRecordInteractor.pauseRecordingFile()
                }
            }
        }
    }
}


