package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.equip

import com.alekseyvalyakin.roleplaysystem.data.analytics.GameAnalyticsEvent
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.uber.rib.core.Bundle

sealed class GameSettingsEquipAnalyticsEvent(name: String, game: Game, bundle: Bundle = Bundle()) : GameAnalyticsEvent(name, game, bundle) {

}