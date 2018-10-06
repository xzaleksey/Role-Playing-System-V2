package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats

import com.alekseyvalyakin.roleplaysystem.data.analytics.*
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats.GameStat
import com.uber.rib.core.Bundle

sealed class GameSettingsStatAnalyticsEvent(name: String, game: Game, bundle: Bundle = Bundle()) : GameAnalyticsEvent(name, game, bundle) {

    class CreateStat(
            game: Game,
            gameStat: GameStat
    ) : GameSettingsStatAnalyticsEvent(CREATE_CUSTOM_STAT, game, Bundle().apply {
        putString(NAME_PARAM, gameStat.getDisplayedName())
    })

    class SelectDefaultStat(
            val game: Game,
            gameStat: GameStat
    ) : GameSettingsStatAnalyticsEvent(SELECT_DEFAULT_STAT, game, Bundle().apply {
        putString(STAT_ID_PARAM, gameStat.id)
    })

    class SelectCustomStat(
            val game: Game,
            gameStat: GameStat
    ) : GameSettingsStatAnalyticsEvent(SELECT_DEFAULT_STAT, game, Bundle().apply {
        putString(STAT_ID_PARAM, gameStat.id)
    })

    class UnselectDefaultStat(
            val game: Game,
            gameStat: GameStat
    ) : GameSettingsStatAnalyticsEvent(UNSELECT_DEFAULT_STAT, game, Bundle().apply {
        putString(STAT_ID_PARAM, gameStat.id)
    })

    class DeleteCustomStat(
            val game: Game,
            gameStat: GameStat
    ) : GameSettingsStatAnalyticsEvent(DELETE_CUSTOM_STAT, game, Bundle().apply {
        putString(STAT_ID_PARAM, gameStat.id)
    })

    class UnselectCustomStat(
            val game: Game,
            gameStat: GameStat
    ) : GameSettingsStatAnalyticsEvent(UNSELECT_CUSTOM_STAT, game, Bundle().apply {
        putString(STAT_ID_PARAM, gameStat.id)
    })

    class UpdateStat(
            val game: Game,
            gameStat: GameStat
    ) : GameSettingsStatAnalyticsEvent(UPDATE_STAT, game, Bundle().apply {
        putString(STAT_ID_PARAM, gameStat.id)
        putString(DEFAULT_PARAM, GameStat.INFO.isSupported(gameStat).toString())
    })

}