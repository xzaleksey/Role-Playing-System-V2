package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.races

import com.alekseyvalyakin.roleplaysystem.data.analytics.CREATE_CUSTOM_RACE
import com.alekseyvalyakin.roleplaysystem.data.analytics.DEFAULT_PARAM
import com.alekseyvalyakin.roleplaysystem.data.analytics.DELETE_CUSTOM_RACE
import com.alekseyvalyakin.roleplaysystem.data.analytics.GameAnalyticsEvent
import com.alekseyvalyakin.roleplaysystem.data.analytics.NAME_PARAM
import com.alekseyvalyakin.roleplaysystem.data.analytics.RACE_ID_PARAM
import com.alekseyvalyakin.roleplaysystem.data.analytics.SELECT_DEFAULT_RACE
import com.alekseyvalyakin.roleplaysystem.data.analytics.UNSELECT_CUSTOM_RACE
import com.alekseyvalyakin.roleplaysystem.data.analytics.UNSELECT_DEFAULT_RACE
import com.alekseyvalyakin.roleplaysystem.data.analytics.UPDATE_RACE
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.races.GameRace
import com.uber.rib.core.Bundle

sealed class GameSettingsRaceAnalyticsEvent(name: String, game: Game, bundle: Bundle = Bundle()) : GameAnalyticsEvent(name, game, bundle) {

    class CreateRace(
            game: Game,
            gameClass: GameRace
    ) : GameSettingsRaceAnalyticsEvent(CREATE_CUSTOM_RACE, game, Bundle().apply {
        putString(NAME_PARAM, gameClass.getDisplayedName())
    })

    class SelectDefaultRace(
            val game: Game,
            gameClass: GameRace
    ) : GameSettingsRaceAnalyticsEvent(SELECT_DEFAULT_RACE, game, Bundle().apply {
        putString(RACE_ID_PARAM, gameClass.id)
    })

    class SelectCustomRace(
            val game: Game,
            gameClass: GameRace
    ) : GameSettingsRaceAnalyticsEvent(SELECT_DEFAULT_RACE, game, Bundle().apply {
        putString(RACE_ID_PARAM, gameClass.id)
    })

    class UnselectDefaultRace(
            val game: Game,
            gameClass: GameRace
    ) : GameSettingsRaceAnalyticsEvent(UNSELECT_DEFAULT_RACE, game, Bundle().apply {
        putString(RACE_ID_PARAM, gameClass.id)
    })

    class DeleteCustomRace(
            val game: Game,
            gameClass: GameRace
    ) : GameSettingsRaceAnalyticsEvent(DELETE_CUSTOM_RACE, game, Bundle().apply {
        putString(RACE_ID_PARAM, gameClass.id)
    })

    class UnselectCustomRace(
            val game: Game,
            gameClass: GameRace
    ) : GameSettingsRaceAnalyticsEvent(UNSELECT_CUSTOM_RACE, game, Bundle().apply {
        putString(RACE_ID_PARAM, gameClass.id)
    })

    class UpdateRace(
            val game: Game,
            gameClass: GameRace
    ) : GameSettingsRaceAnalyticsEvent(UPDATE_RACE, game, Bundle().apply {
        putString(RACE_ID_PARAM, gameClass.id)
        putString(DEFAULT_PARAM, GameRace.INFO.isSupported(gameClass).toString())
    })

}