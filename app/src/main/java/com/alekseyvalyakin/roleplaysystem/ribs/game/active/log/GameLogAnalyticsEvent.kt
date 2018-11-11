package com.alekseyvalyakin.roleplaysystem.ribs.game.active.log

import com.alekseyvalyakin.roleplaysystem.data.analytics.*
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.uber.rib.core.Bundle

sealed class GameLogAnalyticsEvent(name: String, game: Game, bundle: Bundle = Bundle()) : GameAnalyticsEvent(name, game, bundle) {
    class OpenAudio(
            game: Game
    ) : GameLogAnalyticsEvent(OPEN_AUDIO, game)

    class OpenTexts(
            game: Game
    ) : GameLogAnalyticsEvent(OPEN_TEXTS, game)

    class StartRecord(
            game: Game
    ) : GameLogAnalyticsEvent(START_RECORD, game)

    class StopRecord(
            game: Game
    ) : GameLogAnalyticsEvent(STOP_RECORD, game)
}