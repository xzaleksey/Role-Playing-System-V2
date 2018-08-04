package com.alekseyvalyakin.roleplaysystem.ribs.game.create

import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

/**
 * Coordinates Business Logic for [CreateGameScope].
 *
 */
@RibInteractor
class CreateGameInteractor : BaseInteractor<CreateGameInteractor.CreateGamePresenter, CreateGameRouter>() {

    @Inject
    lateinit var presenter: CreateGamePresenter

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

    }

    override fun willResignActive() {
        super.willResignActive()

    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface CreateGamePresenter
}
