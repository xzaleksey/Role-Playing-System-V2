package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records

import com.alekseyvalyakin.roleplaysystem.base.filter.FilterModel
import com.alekseyvalyakin.roleplaysystem.base.filter.FilterModel.Companion.createFromFilterModel
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.sound.AudioFileInteractor
import com.alekseyvalyakin.roleplaysystem.data.sound.SoundRecordInteractor
import com.alekseyvalyakin.roleplaysystem.di.activity.ThreadConfig
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameEvent
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay
import com.uber.rib.core.*
import io.reactivex.Observable
import io.reactivex.Scheduler
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@RibInteractor
class RecordsInteractor : BaseInteractor<RecordsPresenter, RecordsRouter>() {

    @Inject
    lateinit var presenter: RecordsPresenter
    @Inject
    lateinit var activeGameEventRelay: Relay<ActiveGameEvent>
    @field:[Inject ThreadConfig(ThreadConfig.TYPE.UI)]
    lateinit var uiScheduler: Scheduler
    @field:[Inject ThreadConfig(ThreadConfig.TYPE.IO)]
    lateinit var ioScheduler: Scheduler
    @Inject
    lateinit var game: Game
    @Inject
    lateinit var soundRecordInteractor: SoundRecordInteractor
    @Inject
    lateinit var audioFileInteractor: AudioFileInteractor
    @Inject
    lateinit var filterRelay: BehaviorRelay<FilterModel>
    @Inject
    lateinit var analyticsReporter: AnalyticsReporter
    private val screenName = "Records"

    private var model: RecordsViewModel = RecordsViewModel()

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        analyticsReporter.setCurrentScreen(screenName)
        savedInstanceState?.run { model = this.getSerializable(modelKey) }
                ?: run {
                    Timber.d("Attach when first ${savedInstanceState != null}")
                    router.attachLog()
                }

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
                    presenter.updateRecordState(RecordState(it))
                }.addToDisposables()

        audioFileInteractor.observe()
                .observeOn(uiScheduler)
                .subscribeWithErrorLogging {
                    presenter.updateAudioState(it)
                }.addToDisposables()

        presenter.update(model)
    }

    private fun handleUiEvent(uiEvent: RecordsPresenter.UiEvent): Observable<Any> {
        when (uiEvent) {
            is RecordsPresenter.UiEvent.OpenAudio -> {
                return Observable.fromCallable {
                    model = RecordsViewModel(RecordTab.AUDIO)
                    analyticsReporter.logEvent(GameRecordsAnalyticsEvent.OpenAudio(game))
                    router.attachAudio()
                }
            }
            is RecordsPresenter.UiEvent.OpenLogs -> {
                return Observable.fromCallable {
                    model = RecordsViewModel(RecordTab.LOG)
                    analyticsReporter.logEvent(GameRecordsAnalyticsEvent.OpenLogs(game))
                    router.attachLog()
                }
            }
            RecordsPresenter.UiEvent.StopRecording -> {
                return Observable.fromCallable {
                    analyticsReporter.logEvent(GameRecordsAnalyticsEvent.StopRecord(game))
                    soundRecordInteractor.stopRecordingFile()
                }
            }
            is RecordsPresenter.UiEvent.PauseRecording -> {
                return Observable.fromCallable {
                    if (uiEvent.recordState.isInProgress()) {
                        soundRecordInteractor.pauseRecordingFile()
                    } else {
                        soundRecordInteractor.startRecordFile(game.id)
                    }
                }
            }
            is RecordsPresenter.UiEvent.SaveRecord -> {
                return Observable.fromCallable {
                    val finalFile = uiEvent.recordState.recordInfo.finalFile
                    finalFile.renameTo(File(finalFile.parentFile, "${uiEvent.newFileName}.${finalFile.extension}"))
                }.subscribeOn(ioScheduler)
                        .observeOn(uiScheduler)
                        .doOnNext { presenter.showSuccessSave() }
                        .cast(Any::class.java)
            }
            is RecordsPresenter.UiEvent.SearchInput -> {
                return Observable.fromCallable {
                    filterRelay.accept(createFromFilterModel(filterRelay.value, uiEvent.text))
                }
            }
            is RecordsPresenter.UiEvent.TogglePlay -> {
                return Observable.fromCallable {
                    if (!uiEvent.isPlaying) {
                        analyticsReporter.logEvent(GameRecordsAnalyticsEvent.PausePlayingFile(game))
                        audioFileInteractor.pause()
                    } else {
                        analyticsReporter.logEvent(GameRecordsAnalyticsEvent.ResumePlayingFile(game))
                        audioFileInteractor.playFile(uiEvent.file)
                    }
                }
            }
            is RecordsPresenter.UiEvent.SeekTo -> {
                return Observable.fromCallable {
                    analyticsReporter.logEvent(GameRecordsAnalyticsEvent.SeekTo(game, uiEvent.progress))
                    audioFileInteractor.seekTo(uiEvent.progress)
                }
            }
            is RecordsPresenter.UiEvent.DeleteFile -> {
                return Observable.fromCallable {
                    analyticsReporter.logEvent(GameRecordsAnalyticsEvent.DeleteRecord(game))
                    audioFileInteractor.stop()
                    uiEvent.audioState.file.delete()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(modelKey, model)
        super.onSaveInstanceState(outState)
    }
}


