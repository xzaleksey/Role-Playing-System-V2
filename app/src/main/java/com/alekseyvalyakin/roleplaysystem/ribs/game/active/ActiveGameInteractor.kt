package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.model.ActiveGameViewModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.model.ActiveGameViewModelProvider
import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

/**
 * Coordinates Business Logic for [ActiveGameScope].
 *
 * TODO describe the logic of this scope.
 */
@RibInteractor
class ActiveGameInteractor : Interactor<ActiveGameInteractor.ActiveGamePresenter, ActiveGameRouter>() {

    @Inject
    lateinit var presenter: ActiveGamePresenter
    @Inject
    lateinit var viewModelProvider: ActiveGameViewModelProvider

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        presenter.showModel(viewModelProvider.getActiveGameViewModel())
    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface ActiveGamePresenter {
        fun showModel(viewModel: ActiveGameViewModel)
    }
}
