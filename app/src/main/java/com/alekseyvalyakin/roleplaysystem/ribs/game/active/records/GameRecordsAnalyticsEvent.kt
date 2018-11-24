package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records

import com.alekseyvalyakin.roleplaysystem.data.analytics.*
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.uber.rib.core.Bundle

sealed class GameRecordsAnalyticsEvent(name: String, game: Game, bundle: Bundle = Bundle()) : GameAnalyticsEvent(name, game, bundle) {
    class OpenAudio(
            game: Game
    ) : GameRecordsAnalyticsEvent(OPEN_AUDIO, game)

    class OpenLogs(
            game: Game
    ) : GameRecordsAnalyticsEvent(OPEN_LOGS, game)

    class StartRecord(
            game: Game
    ) : GameRecordsAnalyticsEvent(START_RECORD, game)

    class StopRecord(
            game: Game
    ) : GameRecordsAnalyticsEvent(STOP_RECORD, game)
}