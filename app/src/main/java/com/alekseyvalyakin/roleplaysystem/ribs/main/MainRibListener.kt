package com.alekseyvalyakin.roleplaysystem.ribs.main

import com.alekseyvalyakin.roleplaysystem.data.firestore.user.User
import com.alekseyvalyakin.roleplaysystem.data.game.Game

interface MainRibListener {
    fun onMainRibEvent(mainRibEvent: MainRibEvent)

    sealed class MainRibEvent {
        class CreateGame(val game: Game) : MainRibEvent()

        class MyProfile(val user: User) : MainRibEvent()
    }
}