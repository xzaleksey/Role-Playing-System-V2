package com.alekseyvalyakin.roleplaysystem.ribs.main

import com.alekseyvalyakin.roleplaysystem.data.analytics.*
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsEvent
import com.uber.rib.core.Bundle

sealed class MainAnalyticsEvent(name: String, bundle: Bundle? = null) : AnalyticsEvent(name, bundle) {
    object CreateGame : MainAnalyticsEvent(CREATE_GAME)
    object Logout : MainAnalyticsEvent(LOGOUT)

    class GameClick(val game: Game) : MainAnalyticsEvent(GAME_CLICK, Bundle().apply {
        putString(GAME_ID_PARAM, game.id)
        putString(STATUS_PARAM, game.status.toString())
    })

    object ProfileClick : MainAnalyticsEvent(PROFILE_CLICK)
}