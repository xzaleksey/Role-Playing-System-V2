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

    class StopRecord(
            game: Game
    ) : GameRecordsAnalyticsEvent(STOP_RECORD, game)

    class DeleteRecord(
            game: Game
    ) : GameRecordsAnalyticsEvent(DELETE_RECORD, game)

    class PausePlayingFile(
            game: Game
    ) : GameRecordsAnalyticsEvent(PAUSE_PLAYING_FILE, game, Bundle().apply {
        putString(VIEW_PARAM, PLAYER_VIEW)
    })

    class ResumePlayingFile(
            game: Game
    ) : GameRecordsAnalyticsEvent(RESUME_PLAYING_FILE, game, Bundle().apply {
        putString(VIEW_PARAM, PLAYER_VIEW)
    })

    class SeekTo(
            game: Game,
            val progress: Int
    ) : GameRecordsAnalyticsEvent(SEEK_FILE, game, Bundle().apply {
        putString(PROGRESS_PARAM, progress.toString())
        putString(VIEW_PARAM, PLAYER_VIEW)
    })

    companion object {
        const val PLAYER_VIEW = "player_view"
    }
}