package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos

import com.alekseyvalyakin.roleplaysystem.data.analytics.*
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.uber.rib.core.Bundle

sealed class GamePhotoAnalyticsEvent(name: String, game: Game, bundle: Bundle = Bundle()) : GameAnalyticsEvent(name, game, bundle) {
    class OpenPhoto(
            game: Game,
            photoId: String
    ) : GamePhotoAnalyticsEvent(OPEN_PHOTO, game, Bundle().apply {
        putString(PHOTO_ID, photoId)
    })


    class UploadPhoto(
            game: Game
    ) : GamePhotoAnalyticsEvent(UPLOAD_PHOTO, game, Bundle().apply {
    })

    class DeletePhoto(
            game: Game,
            photoId: String
    ) : GamePhotoAnalyticsEvent(DELETE_PHOTO, game, Bundle().apply {
        putString(PHOTO_ID, photoId)
    })

    class SwitchPhotoVisibility(
            game: Game,
            photoId: String,
            visibility: String
    ) : GamePhotoAnalyticsEvent(SWITCH_PHOTO_VISIBILITY, game, Bundle().apply {
        putString(PHOTO_ID, photoId)
        putString(VISIBILITY, visibility)
    })
}