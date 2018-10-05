package com.alekseyvalyakin.roleplaysystem.ribs.profile

import com.alekseyvalyakin.roleplaysystem.data.analytics.CHANGE_USER_NAME
import com.alekseyvalyakin.roleplaysystem.data.analytics.GAME_CLICK
import com.alekseyvalyakin.roleplaysystem.data.analytics.GAME_ID_PARAM
import com.alekseyvalyakin.roleplaysystem.data.analytics.STATUS_PARAM
import com.alekseyvalyakin.roleplaysystem.data.analytics.UPDATE_AVATAR
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsEvent
import com.uber.rib.core.Bundle

sealed class ProfileAnalyticsEvent(name: String, bundle: Bundle? = null) : AnalyticsEvent(name, bundle) {
    object ChangeUserName : ProfileAnalyticsEvent(CHANGE_USER_NAME)
    object UpdateAvatar : ProfileAnalyticsEvent(UPDATE_AVATAR)

    class GameClick(val game: Game) : ProfileAnalyticsEvent(GAME_CLICK, Bundle().apply {
        putString(GAME_ID_PARAM, game.id)
        putString(STATUS_PARAM, game.status.toString())
    })
}