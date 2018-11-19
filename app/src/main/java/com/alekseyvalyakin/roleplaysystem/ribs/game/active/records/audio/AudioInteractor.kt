package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio


import com.alekseyvalyakin.roleplaysystem.data.sound.AudioFileInteractor
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.RecordsRouter
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject

@RibInteractor
class AudioInteractor : BaseInteractor<AudioPresenter, RecordsRouter>() {

    @Inject
    lateinit var presenter: AudioPresenter
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
                        audioFileInteractor.pause()
                    } else {
                        audioFileInteractor.playFile(uiEvent.file)
                    }
                }
            }
            is AudioPresenter.UiEvent.SeekTo -> {
                return Observable.fromCallable {
                  audioFileInteractor.seekTo(uiEvent.progress)
                }
            }

        }
    }

}


