package com.alekseyvalyakin.roleplaysystem.ribs.root

import com.alekseyvalyakin.roleplaysystem.data.firestore.user.User
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.ribs.auth.AuthBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.auth.AuthRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.transition.ActiveGameAttachTransition
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.transition.CreateGameAttachTransition
import com.alekseyvalyakin.roleplaysystem.ribs.main.MainBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.main.MainRouter
import com.alekseyvalyakin.roleplaysystem.ribs.profile.ProfileBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.profile.ProfileRouter
import com.alekseyvalyakin.roleplaysystem.ribs.profile.transition.ProfileAttachTransition
import com.uber.rib.core.DefaultAttachTransition
import com.uber.rib.core.DefaultDetachTransition
import com.uber.rib.core.RouterNavigatorFactory
import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link RootBuilder.RootScope}.
 *
 * Root router of the app
 */
class RootRouter(
        view: RootView,
        interactor: RootInteractor,
        component: RootBuilder.Component,
        routerNavigatorFactory: RouterNavigatorFactory,
        private val authBuilder: AuthBuilder,
        private val mainBuilder: MainBuilder,
        private val createGameBuilder: CreateGameBuilder,
        private val profileBuilder: ProfileBuilder,
        private val activeGameBuilder: ActiveGameBuilder
) : ViewRouter<RootView, RootInteractor, RootBuilder.Component>(view, interactor, component) {

    private val router = routerNavigatorFactory.create<RootState>(this)!!
    private val authAttachTransition = object : DefaultAttachTransition<AuthRouter, RootState, AuthBuilder>(authBuilder, view) {}
    private val authDetachTransition = object : DefaultDetachTransition<AuthRouter, RootState>(view) {}

    private val mainAttachTransition = object : DefaultAttachTransition<MainRouter, RootState, MainBuilder>(mainBuilder, view) {}
    private val mainDetachTransition = object : DefaultDetachTransition<MainRouter, RootState>(view) {}

    private val createGameDetachTransition = object : DefaultDetachTransition<CreateGameRouter, RootState>(view) {}
    private val profileDetachTransition = object : DefaultDetachTransition<ProfileRouter, RootState>(view) {}
    private val activeGameDetachTransition = object : DefaultDetachTransition<ActiveGameRouter, RootState>(view) {}

    fun attachAuth() {
        router.pushTransientState(RootState.AUTH, authAttachTransition, authDetachTransition)
    }

    fun attachMain() {
        val peekState = router.peekState()
        if (peekState == null || peekState == RootState.AUTH) {
            router.pushRetainedState(RootState.MAIN, mainAttachTransition, mainDetachTransition)
        }
    }

    fun attachCreateGame(game: Game) {
        router.pushRetainedState(RootState.CREATE_GAME,
                CreateGameAttachTransition<RootState>(createGameBuilder, view, game),
                createGameDetachTransition)
    }

    fun attachMyProfile(user: User) {
        router.pushRetainedState(RootState.PROFILE, ProfileAttachTransition(profileBuilder, view, user),
                profileDetachTransition)
    }

    fun attachOpenActiveGame(game: Game) {
        router.pushRetainedState(RootState.ACTIVE_GAME,
                ActiveGameAttachTransition<RootState>(activeGameBuilder, view, game),
                activeGameDetachTransition)
    }

    fun onBackPressed(): Boolean {
        val currentRouter = router.peekRouter()
        if (currentRouter != null && currentRouter.handleBackPress()) {
            return true
        }

        router.popState()
        return router.peekState() != null
    }

    fun detachCreateGame() {
        val peekState = router.peekState()
        if (peekState == RootState.CREATE_GAME) {
            router.popState()
        }
    }

}
