package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import com.alekseyvalyakin.roleplaysystem.base.model.NavigationId
import com.alekseyvalyakin.roleplaysystem.data.analytics.GameAnalyticsEvent
import com.alekseyvalyakin.roleplaysystem.data.analytics.NAVIGATE
import com.alekseyvalyakin.roleplaysystem.data.analytics.SCREEN_PARAM
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.uber.rib.core.Bundle

sealed class ActiveGameAnalyticsAnalyticsEvent(name: String, game: Game, bundle: Bundle = Bundle()) : GameAnalyticsEvent(name, game, bundle) {

    class Navigate(game: Game, navigateId: NavigationId) : ActiveGameAnalyticsAnalyticsEvent(NAVIGATE, game, Bundle().apply {
        putString(SCREEN_PARAM, navigateId.textId)
    })
}