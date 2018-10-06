package com.alekseyvalyakin.roleplaysystem.data.analytics

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsEvent
import com.uber.rib.core.Bundle

open class GameAnalyticsEvent(
        name: String,
        game: Game,
        bundle: Bundle = Bundle()
) : AnalyticsEvent(name,
        bundle.apply {
            putString(GAME_ID_PARAM, game.id)
            putString(STATUS_PARAM, game.status.toString())
        }
)