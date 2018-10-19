package com.alekseyvalyakin.roleplaysystem.ribs.main

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.User

interface MainRibListener {
    fun onMainRibEvent(mainRibEvent: MainRibEvent)

    sealed class MainRibEvent {
        class CreateGame(val game: Game) : MainRibEvent()

        class OpenActiveGame(val game: Game) : MainRibEvent()

        class MyProfile(val user: User) : MainRibEvent()

        object NavigateToFeatures : MainRibEvent()

        object NavigateToLicense : MainRibEvent()
    }
}