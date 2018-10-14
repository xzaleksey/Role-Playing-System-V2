package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.spells

import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

@RibInteractor
class GameSettingsSpellsInteractor : BaseInteractor<GameSettingsSpellsPresenter, GameSettingsSpellsRouter>() {

    @Inject
    lateinit var presenter: GameSettingsSpellsPresenter

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
    }

}
