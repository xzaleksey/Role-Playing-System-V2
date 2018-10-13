package com.alekseyvalyakin.roleplaysystem.ribs.game.active.log

import com.alekseyvalyakin.roleplaysystem.data.analytics.*
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.uber.rib.core.Bundle

sealed class GameLogAnalyticsEvent(name: String, game: Game, bundle: Bundle = Bundle()) : GameAnalyticsEvent(name, game, bundle) {
    class OpenLog(
            game: Game,
            photoId: String
    ) : GameLogAnalyticsEvent(OPEN_PHOTO, game, Bundle().apply {
        putString(PHOTO_ID, photoId)
    })

    class ChangeLogName(
            game: Game,
            photoId: String
    ) : GameLogAnalyticsEvent(CHANGE_PHOTO_NAME, game, Bundle().apply {
        putString(PHOTO_ID, photoId)
    })

    class UploadLog(
            game: Game
    ) : GameLogAnalyticsEvent(UPLOAD_PHOTO, game, Bundle().apply {
    })

    class DeleteLog(
            game: Game,
            photoId: String
    ) : GameLogAnalyticsEvent(DELETE_PHOTO, game, Bundle().apply {
        putString(PHOTO_ID, photoId)
    })

    class SwitchLogVisibility(
            game: Game,
            photoId: String,
            visibility: String
    ) : GameLogAnalyticsEvent(SWITCH_PHOTO_VISIBILITY, game, Bundle().apply {
        putString(PHOTO_ID, photoId)
        putString(VISIBILITY, visibility)
    })
}