package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos

import com.alekseyvalyakin.roleplaysystem.data.analytics.GAME_ID_PARAM
import com.alekseyvalyakin.roleplaysystem.data.analytics.NAVIGATE
import com.alekseyvalyakin.roleplaysystem.data.analytics.STATUS_PARAM
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsEvent
import com.uber.rib.core.Bundle

sealed class GamePhotoAnalyticsEvent(name: String, bundle: Bundle? = null) : AnalyticsEvent(name, bundle) {
    class Navigate(
            val game: Game
    ) : GamePhotoAnalyticsEvent(NAVIGATE, Bundle().apply {
        putString(GAME_ID_PARAM, game.id)
        putString(STATUS_PARAM, game.status.toString())
    })

}