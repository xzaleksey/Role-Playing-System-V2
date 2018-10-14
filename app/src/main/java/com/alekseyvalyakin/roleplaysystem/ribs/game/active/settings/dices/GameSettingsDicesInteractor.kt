package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.dices

import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

@RibInteractor
class GameSettingsDicesInteractor : BaseInteractor<GameSettingsDicesPresenter, GameSettingsDicesRouter>() {

    @Inject
    lateinit var presenter: GameSettingsDicesPresenter

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
    }

}
