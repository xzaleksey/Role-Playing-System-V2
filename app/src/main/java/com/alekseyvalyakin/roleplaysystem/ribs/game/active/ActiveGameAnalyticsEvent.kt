package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import com.alekseyvalyakin.roleplaysystem.base.model.NavigationId
import com.alekseyvalyakin.roleplaysystem.data.analytics.GAME_ID_PARAM
import com.alekseyvalyakin.roleplaysystem.data.analytics.NAVIGATE
import com.alekseyvalyakin.roleplaysystem.data.analytics.SCREEN_PARAM
import com.alekseyvalyakin.roleplaysystem.data.analytics.STATUS_PARAM
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsEvent
import com.uber.rib.core.Bundle

sealed class ActiveGameAnalyticsEvent(name: String, bundle: Bundle? = null) : AnalyticsEvent(name, bundle) {

    class Navigate(game: Game, navigateId: NavigationId) : ActiveGameAnalyticsEvent(NAVIGATE, Bundle().apply {
        putString(GAME_ID_PARAM, game.id)
        putString(STATUS_PARAM, game.status.toString())
        putString(SCREEN_PARAM, navigateId.textId)
    })
}