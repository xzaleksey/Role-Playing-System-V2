package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import com.alekseyvalyakin.roleplaysystem.base.model.NavigationId
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.model.ActiveGameViewModelProvider
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import timber.log.Timber
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

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        presenter.showModel(viewModelProvider.getActiveGameViewModel())
        presenter.observeUiEvents()
                .subscribeWithErrorLogging {
                    when (it) {
                        is ActiveGamePresenter.Event.Navigate -> {
                            val navigationId = NavigationId.findById(it.id)
                            Timber.d("id %s", navigationId)
                            router.attachView(navigationId)
                        }
                    }
                }.addToDisposables()
    }

}
