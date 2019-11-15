package com.alekseyvalyakin.roleplaysystem.ribs.root

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.User
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameParams
import com.uber.rib.core.SerializableRouterNavigatorState
import java.io.Serializable

sealed class RootState(val name: String, val navigationId: NavigationId) : SerializableRouterNavigatorState {

    override fun name(): String {
        return name
    }

    class AUTH : RootState(AUTH_NAME, NavigationId.AUTH)
    class MAIN : RootState(MAIN_NAME, NavigationId.MAIN)
    class FEATURES : RootState(FEATURES_NAME, NavigationId.FEATURES)
    class LICENSE : RootState(LICENSE_NAME, NavigationId.LICENSE)

    class ActiveGame(private val activeGameParams: ActiveGameParams) : RootState("Active game", NavigationId.ACTIVE_GAME) {
        override fun getRestorableInfo(): Serializable? {
            return activeGameParams
        }
    }

    class CreateGame(val game: Game) : RootState(CREATE_GAME_NAME, NavigationId.CREATE_GAME) {
        override fun getRestorableInfo(): Serializable? {
            return game
        }
    }

    class PROFILE(val user: User) : RootState(PROFILE_NAME, NavigationId.PROFILE) {
        override fun getRestorableInfo(): Serializable? {
            return user
        }
    }

    companion object {
        const val AUTH_NAME = "Auth"
        const val MAIN_NAME = "Main"
        const val FEATURES_NAME = "Features"
        const val LICENSE_NAME = "License"
        const val CREATE_GAME_NAME = "Create game"
        const val PROFILE_NAME = "Profile"
        const val ACTIVE_GAME_NAME = "Active game"
    }
}