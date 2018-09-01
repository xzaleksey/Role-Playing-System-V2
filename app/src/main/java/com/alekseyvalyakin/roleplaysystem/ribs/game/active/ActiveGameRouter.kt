package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import com.alekseyvalyakin.roleplaysystem.base.model.NavigationId
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.DiceBuilder
import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link ActiveGameBuilder.ActiveGameScope}.
 *
 */
class ActiveGameRouter(
        view: ActiveGameView,
        interactor: ActiveGameInteractor,
        component: ActiveGameBuilder.Component,
        private val diceBuilder: DiceBuilder) : ViewRouter<ActiveGameView, ActiveGameInteractor, ActiveGameBuilder.Component>(view, interactor, component) {

    fun attachView(navigationId: NavigationId) {
        when (navigationId) {
            NavigationId.DICES -> {
                val router = diceBuilder.build(view)
                attachChild(router)

                val contentContainer = view.getContentContainer()
                contentContainer.removeAllViews()
                contentContainer.addView(router.view)
            }
        }
    }


}
