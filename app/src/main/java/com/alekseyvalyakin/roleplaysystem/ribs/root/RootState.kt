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

    class AUTH : RootState("Auth", NavigationId.AUTH)
    class MAIN : RootState("Main", NavigationId.MAIN)
    class FEATURES : RootState("Features", NavigationId.FEATURES)
    class LICENSE : RootState("License", NavigationId.LICENSE)

    class ActiveGame(private val activeGameParams: ActiveGameParams) : RootState("Active game", NavigationId.ACTIVE_GAME) {
        override fun getRestorableInfo(): Serializable? {
            return activeGameParams
        }
    }

    class CreateGame(val game: Game) : RootState("Create game", NavigationId.CREATE_GAME) {
        override fun getRestorableInfo(): Serializable? {
            return game
        }
    }

    class PROFILE(val user: User) : RootState("Profile", NavigationId.PROFILE) {
        override fun getRestorableInfo(): Serializable? {
            return user
        }
    }
}