package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.log

import com.alekseyvalyakin.roleplaysystem.data.analytics.GameAnalyticsEvent
import com.alekseyvalyakin.roleplaysystem.data.analytics.START_RECORD
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.uber.rib.core.Bundle

sealed class GameLogAnalyticsEvent(name: String, game: Game, bundle: Bundle = Bundle()) : GameAnalyticsEvent(name, game, bundle) {

    class StartRecord(
            game: Game
    ) : GameLogAnalyticsEvent(START_RECORD, game)
}