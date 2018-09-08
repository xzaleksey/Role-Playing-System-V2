package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import com.alekseyvalyakin.roleplaysystem.base.model.NavigationId
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.DiceBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.DiceRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.model.ActiveGameViewModelProvider
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.transition.ActiveGameInternalAttachTransition
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.transition.DefaultActiveGameInternalDetachTransition
import com.uber.rib.core.RestorableRouter
import com.uber.rib.core.RouterNavigatorFactory
import com.uber.rib.core.RouterNavigatorState
import com.uber.rib.core.ViewRouter
import java.io.Serializable

/**
 * Adds and removes children of {@link ActiveGameBuilder.ActiveGameScope}.
 *
 */
class ActiveGameRouter(
        view: ActiveGameView,
        interactor: ActiveGameInteractor,
        component: ActiveGameBuilder.Component,
        private val diceBuilder: DiceBuilder,
        private val routerNavigatorFactory: RouterNavigatorFactory,
        private val activeGameViewModelProvider: ActiveGameViewModelProvider
) : ViewRouter<ActiveGameView, ActiveGameInteractor, ActiveGameBuilder.Component>(view, interactor, component), RestorableRouter {

    private val modernRouter = routerNavigatorFactory.create<State>(this)
    private val dicesAttachTransition = ActiveGameInternalAttachTransition(diceBuilder, view)
    private val dicesDetachTransition = object : DefaultActiveGameInternalDetachTransition<DiceRouter, State>(view) {}

    fun attachView(navigationId: NavigationId) {
        when (navigationId) {
            NavigationId.DICES -> {
                modernRouter.pushTransientState(State.DICES, dicesAttachTransition, dicesDetachTransition)
            }
        }
    }

    fun backPress(): Boolean {
        if (modernRouter.peekState() == null) {
            return false
        }

        if (modernRouter.peekRouter()?.handleBackPress() == false) {
            return false
        }

        return true
    }

    override fun getRestorableInfo(): Serializable? {
        return activeGameViewModelProvider.getCurrentGame()
    }

    data class State(val name: String) : RouterNavigatorState {

        override fun name(): String {
            return name
        }

        companion object {
            val DICES = State("DICES")
        }
    }
}
