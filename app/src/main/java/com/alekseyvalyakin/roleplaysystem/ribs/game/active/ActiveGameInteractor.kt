package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import com.alekseyvalyakin.roleplaysystem.base.model.NavigationId
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.GameRepository
import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityListener
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameInteractor.Model.Companion.KEY
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.model.ActiveGameViewModelProvider
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.uber.rib.core.*
import io.reactivex.Observable
import timber.log.Timber
import java.io.Serializable
import javax.inject.Inject

/**
 * Coordinates Business Logic for [ActiveGameScope].
 *
 */
@RibInteractor
class ActiveGameInteractor : BaseInteractor<ActiveGamePresenter, ActiveGameRouter>() {

    @Inject
    lateinit var presenter: ActiveGamePresenter
    @Inject
    lateinit var viewModelProvider: ActiveGameViewModelProvider
    @Inject
    lateinit var activityListener: ActivityListener

    @Inject
    lateinit var gameRepository: GameRepository
    @Inject
    lateinit var activeGameEventObservable: Observable<ActiveGameEvent>
    @Inject
    lateinit var analyticsReporter: AnalyticsReporter

    private val screenName = "ActiveGame"
    private lateinit var model: Model

    private fun getDefaultModel(): Model {
        return if (viewModelProvider.isMaster()) {
            Model(NavigationId.RECORDS)
        } else {
            Model(NavigationId.CHARACTERS)
        }
    }

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        analyticsReporter.setCurrentScreen(screenName)

        model = savedInstanceState?.run {
            getSerializable<Model>(KEY)
        } ?: getDefaultModel().apply {
            handleNavigation(this.navigationId)
        }

        if (savedInstanceState == null) {
            gameRepository.updateDate(viewModelProvider.getCurrentGame())
                    .subscribeWithErrorLogging()
                    .addToDisposables()
        }

        gameRepository.observeDocumentDelete(viewModelProvider.getCurrentGame().id)
                .subscribeWithErrorLogging {
                    Timber.d("Game deleted")
                    router.onDelete()
                    activityListener.backPress()
                }
                .addToDisposables()

        presenter.showModel(viewModelProvider.getActiveGameViewModel(model.navigationId))
        presenter.observeUiEvents()
                .subscribeWithErrorLogging {
                    when (it) {
                        is ActiveGamePresenter.Event.Navigate -> {
                            val navigationId = NavigationId.findById(it.id)
                            model = Model(navigationId)
                            analyticsReporter.logEvent(
                                    ActiveGameAnalyticsAnalyticsEvent.Navigate(
                                            viewModelProvider.getCurrentGame(),
                                            navigationId))
                            handleNavigation(navigationId)
//                            if (navigationId == NavigationId.MENU) {
//                                gameRepository.deleteDocumentOffline(viewModelProvider.getCurrentGame().id)
//                            }
                        }
                    }
                }.addToDisposables()
        activeGameEventObservable.subscribeWithErrorLogging {
            when (it) {
                is ActiveGameEvent.OpenFullSizePhoto -> {
                    router.attachFullSizePhoto(it.fullSizePhotoModel)
                }
                is ActiveGameEvent.HideBottomBar -> {
                    presenter.hideBottomBar()
                }
                is ActiveGameEvent.ShowBottomBar -> {
                    presenter.showBottomBar()
                }
            }
        }.addToDisposables()

    }

    private fun handleNavigation(navigationId: NavigationId) {
        router.attachView(navigationId)
    }

    override fun handleBackPress(): Boolean {
        val backPressed = router.backPress()
        if (backPressed) {
            model = Model(router.getCurrentNavigationId())
            presenter.updateNavigationId(model.navigationId)
        }
        return backPressed
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY, model)
    }

    override fun <T : Router<out Interactor<*, *>, out InteractorBaseComponent<*>>> restoreRouter(clazz: Class<T>, childInfo: Serializable?) {
    }

    override fun getRestorableInfo(): Serializable? {
        return viewModelProvider.getCurrentGame()
    }

    data class Model(
            val navigationId: NavigationId
    ) : Serializable {

        companion object {
            const val KEY = "modelKey"
        }
    }

}
