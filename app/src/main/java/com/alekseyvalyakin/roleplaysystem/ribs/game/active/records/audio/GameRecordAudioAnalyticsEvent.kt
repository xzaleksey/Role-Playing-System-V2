package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio

import com.alekseyvalyakin.roleplaysystem.data.analytics.*
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.uber.rib.core.Bundle

sealed class GameRecordAudioAnalyticsEvent(name: String, game: Game, bundle: Bundle = Bundle()) : GameAnalyticsEvent(name, game, bundle) {

    class StartPlayingFile(
            game: Game
    ) : GameRecordAudioAnalyticsEvent(START_PLAYING_FILE, game, Bundle().apply {
        putString(VIEW_PARAM, VIEW)
    })

    class PausePlayingFile(
            game: Game
    ) : GameRecordAudioAnalyticsEvent(PAUSE_PLAYING_FILE, game, Bundle().apply {
        putString(VIEW_PARAM, VIEW)
    })

    class ResumePlayingFile(
            game: Game
    ) : GameRecordAudioAnalyticsEvent(RESUME_PLAYING_FILE, game, Bundle().apply {
        putString(VIEW_PARAM, VIEW)
    })

    companion object {
        const val VIEW = "audio_list"
    }
}