package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings

import com.alekseyvalyakin.roleplaysystem.data.analytics.GAME_ID_PARAM
import com.alekseyvalyakin.roleplaysystem.data.analytics.NAVIGATE
import com.alekseyvalyakin.roleplaysystem.data.analytics.SCREEN_PARAM
import com.alekseyvalyakin.roleplaysystem.data.analytics.SKIP
import com.alekseyvalyakin.roleplaysystem.data.analytics.STATUS_PARAM
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsEvent
import com.uber.rib.core.Bundle

sealed class GameSettingsAnalyticsEvent(name: String, bundle: Bundle? = null) : AnalyticsEvent(name, bundle) {
    object Skip : GameSettingsAnalyticsEvent(SKIP)

    class Navigate(
            val game: Game,
            gameSettingsItemType: GameSettingsViewModel.GameSettingsItemType
    ) : GameSettingsAnalyticsEvent(NAVIGATE, Bundle().apply {
        putString(GAME_ID_PARAM, game.id)
        putString(STATUS_PARAM, game.status.toString())
        putString(SCREEN_PARAM, gameSettingsItemType.textId)
    })

}