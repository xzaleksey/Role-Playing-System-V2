package com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

@RibInteractor
class MenuInteractor : BaseInteractor<MenuPresenter, MenuRouter>() {

    @Inject
    lateinit var presenter: MenuPresenter
    @Inject
    lateinit var game: Game
    @Inject
    lateinit var analyticsReporter: AnalyticsReporter
    private val screenName = "Menu"

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        analyticsReporter.setCurrentScreen(screenName)
    }
}


