package com.alekseyvalyakin.roleplaysystem.ribs.root

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.User
import com.alekseyvalyakin.roleplaysystem.ribs.auth.AuthBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.auth.AuthRouter
import com.alekseyvalyakin.roleplaysystem.ribs.features.FeaturesBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.features.FeaturesRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameParams
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.transition.ActiveGameAttachTransition
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.transition.CreateGameAttachTransition
import com.alekseyvalyakin.roleplaysystem.ribs.license.LicenseBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.license.LicenseRouter
import com.alekseyvalyakin.roleplaysystem.ribs.main.MainBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.main.MainRouter
import com.alekseyvalyakin.roleplaysystem.ribs.profile.ProfileBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.profile.ProfileRouter
import com.alekseyvalyakin.roleplaysystem.ribs.profile.transition.ProfileAttachTransition
import com.uber.rib.core.AttachInfo
import com.uber.rib.core.BaseRouter
import com.uber.rib.core.DefaultAttachTransition
import com.uber.rib.core.DefaultDetachTransition
import timber.log.Timber

/**
 * Adds and removes children of {@link RootBuilder.RootScope}.
 *
 * Root router of the app
 */
class RootRouter(
        view: RootView,
        interactor: RootInteractor,
        component: RootBuilder.Component,
        private val authBuilder: AuthBuilder,
        private val mainBuilder: MainBuilder,
        private val createGameBuilder: CreateGameBuilder,
        private val profileBuilder: ProfileBuilder,
        private val activeGameBuilder: ActiveGameBuilder,
        private val featuresBuilder: FeaturesBuilder,
        private val licenseBuilder: LicenseBuilder
) : BaseRouter<RootView, RootInteractor, RootState, RootBuilder.Component>(view, interactor, component) {

    private val authAttachTransition = object : DefaultAttachTransition<RootState, AuthBuilder>(authBuilder, view) {}
    private val authDetachTransition = DefaultDetachTransition<RootState>(view)

    private val mainAttachTransition = object : DefaultAttachTransition<RootState, MainBuilder>(mainBuilder, view) {}
    private val mainDetachTransition = DefaultDetachTransition<RootState>(view)

    private val createGameDetachTransition = DefaultDetachTransition<RootState>(view)
    private val profileDetachTransition = DefaultDetachTransition<RootState>(view)
    private val activeGameDetachTransition = DefaultDetachTransition<RootState>(view)

    private val featuresAttachTransition = object : DefaultAttachTransition<RootState, FeaturesBuilder>(featuresBuilder, view) {}
    private val featuresDetachTransition = DefaultDetachTransition<RootState>(view)

    private val licenseAttachTransition = object : DefaultAttachTransition<RootState, LicenseBuilder>(licenseBuilder, view) {}
    private val licenseDetachTransition = DefaultDetachTransition<RootState>(view)


    override fun initNavigator(navigator: MutableMap<String, (AttachInfo<RootState>) -> Boolean>) {
        navigator[RootState.PROFILE_NAME] = {
            internalPushRetainedState(it.state, ProfileAttachTransition(profileBuilder, view, it.state.getRestorableInfo() as User), profileDetachTransition)
        }
        navigator[RootState.CREATE_GAME_NAME] = {
            internalPushRetainedState(it.state, CreateGameAttachTransition(createGameBuilder, view, it.state.getRestorableInfo() as Game),
                    createGameDetachTransition)
        }
        navigator[RootState.AUTH_NAME] = {
            internalPushTransientState(it.state, authAttachTransition, authDetachTransition)
        }
        navigator[RootState.MAIN_NAME] = {
            internalPushRetainedState(it.state, mainAttachTransition, mainDetachTransition)
        }
        navigator[RootState.ACTIVE_GAME_NAME] = {
            internalPushRetainedState(it.state, ActiveGameAttachTransition(activeGameBuilder, view, it.state.getRestorableInfo() as ActiveGameParams), activeGameDetachTransition)
        }
        navigator[RootState.LICENSE_NAME] = {
            internalPushRetainedState(it.state, licenseAttachTransition, licenseDetachTransition)
        }
        navigator[RootState.FEATURES_NAME] = {
            internalPushRetainedState(it.state, featuresAttachTransition, featuresDetachTransition)
        }
    }

    fun attachAuth() {
        attachRib(AttachInfo(RootState.AUTH(), false))
    }

    fun attachMain() {
        val peekState = peekState()
        if (peekState == null || peekState.navigationId == NavigationId.AUTH) {
            Timber.d("Attach main")
            attachRib(AttachInfo(RootState.MAIN(), false))
        }
    }

    fun attachGame(game: Game, isFirstOpen: Boolean = false) {
        if (game.isDraft()) {
            attachRib(AttachInfo(RootState.CreateGame(game), false))
        } else {
            attachRib(AttachInfo(RootState.ActiveGame(ActiveGameParams(game, isFirstOpen)), false))
        }
    }

    fun attachMyProfile(user: User) {
        attachRib(AttachInfo(RootState.PROFILE(user), false))
    }

    override fun hasOwnContent(): Boolean {
        return false
    }

    fun detachCreateGame() {
        val peekState = peekState()
        if (peekState?.navigationId == NavigationId.CREATE_GAME) {
            popState()
        }
    }

    fun attachMyFeatures() {
        attachRib(AttachInfo(RootState.FEATURES(), false))
    }

    fun attachLicense() {
        attachRib(AttachInfo(RootState.LICENSE(), false))
    }

}
