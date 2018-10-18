package com.alekseyvalyakin.roleplaysystem.ribs.features

import com.alekseyvalyakin.roleplaysystem.data.firestore.features.FeaturesRepository
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import io.reactivex.Observable
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
    @Inject
    lateinit var viewModelProvider: FeaturesViewModelProvider

    private val screenName = "Features"
    private var blockVotes = false

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        analyticsReporter.setCurrentScreen(screenName)

        presenter.observeUiEvents()
                .flatMap(this::handleUiEvent)
                .subscribeWithErrorLogging()
                .addToDisposables()

        viewModelProvider.subscribe().subscribeWithErrorLogging {
            presenter.update(it)
        }.addToDisposables()
    }

    private fun handleUiEvent(uiEvent: FeaturesPresenter.UiEvent): Observable<*> {
        return when (uiEvent) {
            is FeaturesPresenter.UiEvent.Vote -> {
                Observable.fromCallable {
                    if (!blockVotes) {
                        presenter.showConfirmDialog(uiEvent.feature)
                    }
                }
            }

            is FeaturesPresenter.UiEvent.ConfirmVote -> {
                blockVotes = true
                presenter.showLoading(true)
                featuresRepository.voteForFeature(uiEvent.feature)
                        .onErrorComplete {
                            Timber.e(it)
                            return@onErrorComplete true
                        }
                        .doOnComplete {
                            presenter.showLoading(false)
                            blockVotes = false
                        }
                        .toObservable<Any>()
            }
        }
    }

}
