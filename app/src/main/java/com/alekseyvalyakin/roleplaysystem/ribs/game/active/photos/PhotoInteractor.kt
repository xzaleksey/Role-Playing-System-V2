package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos

import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

/**
 * Coordinates Business Logic for [PhotoScope].
 *
 */
@RibInteractor
class PhotoInteractor : BaseInteractor<PhotoPresenter, PhotoRouter>() {

    @Inject
    lateinit var presenter: PhotoPresenter

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

    }

    override fun willResignActive() {
        super.willResignActive()
    }

}
