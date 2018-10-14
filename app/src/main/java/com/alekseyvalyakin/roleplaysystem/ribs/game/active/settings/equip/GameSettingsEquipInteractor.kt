package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.equip

import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

@RibInteractor
class GameSettingsEquipInteractor : BaseInteractor<GameSettingsEquipPresenter, GameSettingsEquipRouter>() {

    @Inject
    lateinit var presenter: GameSettingsEquipPresenter

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
    }

}
