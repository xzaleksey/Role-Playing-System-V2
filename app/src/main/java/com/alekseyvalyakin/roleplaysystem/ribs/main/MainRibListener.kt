package com.alekseyvalyakin.roleplaysystem.ribs.main

interface MainRibListener {
    fun onMainRibEvent(mainRibEvent: MainRibEvent)

    sealed class MainRibEvent {
        class CreateGame : MainRibEvent()
    }
}