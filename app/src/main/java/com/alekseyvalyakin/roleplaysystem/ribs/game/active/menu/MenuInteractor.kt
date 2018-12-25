package com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
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
    lateinit var mainViewModelProvider: MenuViewModelProvider
    @Inject
    lateinit var analyticsReporter: AnalyticsReporter
    private val screenName = "Menu"

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        analyticsReporter.setCurrentScreen(screenName)
        mainViewModelProvider.observeViewModel()
                .subscribeWithErrorLogging {
                    presenter.update(it)
                }.addToDisposables()
        presenter.observeUiEvents()
                .subscribeWithErrorLogging { event ->
                    when (event) {
                        is MenuPresenter.UiEvent.OpenProfile -> {
                            router.openProfile(event.user)
                        }
                    }
                }.addToDisposables()
    }

    override fun handleBackPress(): Boolean {
        return router.onBackPressed()
    }
}


