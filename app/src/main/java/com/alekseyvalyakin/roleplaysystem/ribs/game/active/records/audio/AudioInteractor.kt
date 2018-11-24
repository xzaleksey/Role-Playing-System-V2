package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio


import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.sound.AudioFileInteractor
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.RecordsRouter
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import timber.log.Timber
import javax.inject.Inject

@RibInteractor
class AudioInteractor : BaseInteractor<AudioPresenter, RecordsRouter>() {

    @Inject
    lateinit var presenter: AudioPresenter
    @Inject
    lateinit var game: Game
    @Inject
    lateinit var analyticsReporter: AnalyticsReporter
    @Inject
    lateinit var audioViewModelProvider: AudioViewModelProvider
    @Inject
    lateinit var audioFileInteractor: AudioFileInteractor

    private val screenName = "Records_audio"

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        analyticsReporter.setCurrentScreen(screenName)
        audioViewModelProvider.observeViewModel()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWithErrorLogging { presenter.update(it) }
                .addToDisposables()
        presenter.observe()
                .flatMap {
                    return@flatMap handleUiEvent(it).onErrorReturn { t ->
                        Timber.e(t)
                    }
                }
                .subscribeWithErrorLogging { }
    }

    private fun handleUiEvent(uiEvent: AudioPresenter.UiEvent): Observable<Any> {
        when (uiEvent) {
            is AudioPresenter.UiEvent.TogglePlay -> {
                return Observable.fromCallable {
                    if (!uiEvent.isPlaying) {
                        analyticsReporter.logEvent(GameRecordAudioAnalyticsEvent.PausePlayingFile(game))
                        audioFileInteractor.pause()
                    } else {
                        if (audioFileInteractor.currentState().file != uiEvent.file) {
                            analyticsReporter.logEvent(GameRecordAudioAnalyticsEvent.StartPlayingFile(game))
                        } else {
                            analyticsReporter.logEvent(GameRecordAudioAnalyticsEvent.ResumePlayingFile(game))
                        }
                        audioFileInteractor.playFile(uiEvent.file)
                    }
                }
            }
            is AudioPresenter.UiEvent.DeleteFile -> {
                return Observable.fromCallable {
                    audioFileInteractor.stop()
                    uiEvent.audioState.file.delete()
                }
            }

        }
    }

}


