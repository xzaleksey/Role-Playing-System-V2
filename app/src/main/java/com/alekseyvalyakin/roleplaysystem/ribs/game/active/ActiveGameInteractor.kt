package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import com.alekseyvalyakin.roleplaysystem.base.model.NavigationId
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.GameRepository
import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityListener
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameInteractor.Model.Companion.KEY
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.DiceRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.model.ActiveGameViewModelProvider
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.uber.rib.core.*
import timber.log.Timber
import java.io.Serializable
import javax.inject.Inject

/**
 * Coordinates Business Logic for [ActiveGameScope].
 *
 * TODO describe the logic of this scope.
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

    private var model: Model = Model(NavigationId.CHARACTERS)

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        savedInstanceState?.run {
            model = this.getSerializable(KEY)
            handleNavigation(model.navigationId)
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
                            Timber.d("id %s", navigationId)
                            handleNavigation(navigationId)
                            if (navigationId == NavigationId.MENU) {
                                gameRepository.deleteDocumentOffline(viewModelProvider.getCurrentGame().id)
                            }
                        }
                    }
                }.addToDisposables()
    }

    private fun handleNavigation(navigationId: NavigationId) {
        router.attachView(navigationId)
    }

    override fun handleBackPress(): Boolean {
        return router.backPress()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY, model)
    }

    override fun <T : Router<out Interactor<*, *>, out InteractorBaseComponent<*>>> restoreRouter(clazz: Class<T>, childInfo: Serializable?) {
        if (clazz == DiceRouter::class.java) {
            Timber.d("Restored create game Router")
            router.attachView(NavigationId.DICES)
        }

    }

    data class Model(
            val navigationId: NavigationId
    ) : Serializable {

        companion object {
            const val KEY = "modelKey"
        }
    }

}
