package com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.User
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameEvent
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameParams
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.transition.ActiveGameAttachTransition
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameListener
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.transition.CreateGameAttachTransition
import com.alekseyvalyakin.roleplaysystem.ribs.profile.ProfileBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.profile.ProfileListener
import com.alekseyvalyakin.roleplaysystem.ribs.profile.ProfileRouter
import com.alekseyvalyakin.roleplaysystem.ribs.profile.transition.ProfileAttachTransition
import com.jakewharton.rxrelay2.Relay
import com.uber.rib.core.AttachInfo
import com.uber.rib.core.BaseRouter
import com.uber.rib.core.DefaultDetachTransition
import com.uber.rib.core.SerializableRouterNavigatorState
import java.io.Serializable

/**
 * Adds and removes children of {@link PhotoBuilder.PhotoScope}.
 *
 */
class MenuRouter(
        view: MenuView,
        interactor: MenuInteractor,
        component: MenuBuilder.Component,
        private val profileBuilder: ProfileBuilder,
        private val createGameBuilder: CreateGameBuilder,
        private val activeGameBuilder: ActiveGameBuilder,
        private val relay: Relay<ActiveGameEvent>
) : BaseRouter<MenuView, MenuInteractor, MenuRouter.State, MenuBuilder.Component>(view, interactor, component), ProfileListener, CreateGameListener {

    private val navigator = mutableMapOf<NavigationId, (AttachInfo<State>) -> Unit>()
    private val profileDetachTransition = DefaultDetachTransition<ProfileRouter, State>(view)
    private val createGameDetachTransition = DefaultDetachTransition<CreateGameRouter, State>(view)
    private val activeGameDetachTransition = DefaultDetachTransition<ActiveGameRouter, State>(view)

    init {
        navigator[NavigationId.PROFILE] = {
            pushRetainedState(it.state, ProfileAttachTransition(profileBuilder, view, it.state.getRestorableInfo() as User), profileDetachTransition)
        }
        navigator[NavigationId.CREATE_GAME] = {
            pushRetainedState(it.state, CreateGameAttachTransition(createGameBuilder, view, it.state.getRestorableInfo() as Game),
                    createGameDetachTransition)
        }
        navigator[NavigationId.ACTIVE_GAME] = {
            pushRetainedState(it.state, ActiveGameAttachTransition(activeGameBuilder, view, it.state.getRestorableInfo() as ActiveGameParams), activeGameDetachTransition)
        }
    }

    fun openProfile(user: User) {
        attachRib(AttachInfo(State.Profile(user)))
    }

    override fun attachRib(attachInfo: AttachInfo<State>) {
        relay.accept(ActiveGameEvent.HideBottomBar)
        navigator[attachInfo.state.navigationId]?.invoke(attachInfo)
    }

    override fun openGame(game: Game) {
        if (game.isDraft()) {
            attachRib(AttachInfo(State.CreateGame(game), false))
        } else {
            attachRib(AttachInfo(State.ActiveGame(ActiveGameParams(game, false)), false))
        }
    }

    override fun onCreateGameEvent(createGameEvent: CreateGameListener.CreateGameEvent) {
        detachCreateGame()
        when (createGameEvent) {
            is CreateGameListener.CreateGameEvent.CompleteCreate -> {
                attachRib(AttachInfo(State.ActiveGame(ActiveGameParams(createGameEvent.game, false)), false))
            }
        }
    }

    private fun detachCreateGame() {
        val peekState = peekState()
        if (peekState?.navigationId == NavigationId.CREATE_GAME) {
            popState()
        }
    }

    override fun onBackPressed(): Boolean {
        val onBackPressed = super.onBackPressed()
        if (isEmptyStack()) {
            relay.accept(ActiveGameEvent.ShowBottomBar)
        }
        return onBackPressed
    }

    sealed class State(val name: String, val navigationId: NavigationId) : SerializableRouterNavigatorState {

        override fun name(): String {
            return name
        }

        class Profile(val user: User) : State("Profile", NavigationId.PROFILE) {
            override fun getRestorableInfo(): Serializable? {
                return user
            }
        }

        class ActiveGame(private val activeGameParams: ActiveGameParams) : State("Active game", NavigationId.ACTIVE_GAME) {
            override fun getRestorableInfo(): Serializable? {
                return activeGameParams
            }
        }

        class CreateGame(val game: Game) : State("Create game", NavigationId.CREATE_GAME) {
            override fun getRestorableInfo(): Serializable? {
                return game
            }
        }
    }

    enum class NavigationId {
        PROFILE,
        CREATE_GAME,
        ACTIVE_GAME,
    }
}
