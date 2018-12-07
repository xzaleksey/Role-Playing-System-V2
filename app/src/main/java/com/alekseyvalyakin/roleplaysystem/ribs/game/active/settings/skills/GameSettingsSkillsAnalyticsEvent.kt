package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills

import com.alekseyvalyakin.roleplaysystem.data.analytics.*
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.races.GameRace
import com.uber.rib.core.Bundle

sealed class GameSettingsSkillsAnalyticsEvent(name: String, game: Game, bundle: Bundle = Bundle()) : GameAnalyticsEvent(name, game, bundle) {

    class CreateSkill(
            game: Game,
            gameClass: GameRace
    ) : GameSettingsSkillsAnalyticsEvent(CREATE_CUSTOM_RACE, game, Bundle().apply {
        putString(NAME_PARAM, gameClass.getDisplayedName())
    })

    class SelectDefaultRace(
            val game: Game,
            gameClass: GameRace
    ) : GameSettingsSkillsAnalyticsEvent(SELECT_DEFAULT_RACE, game, Bundle().apply {
        putString(RACE_ID_PARAM, gameClass.id)
    })

    class SelectCustomRace(
            val game: Game,
            gameClass: GameRace
    ) : GameSettingsSkillsAnalyticsEvent(SELECT_DEFAULT_RACE, game, Bundle().apply {
        putString(RACE_ID_PARAM, gameClass.id)
    })

    class UnselectDefaultRace(
            val game: Game,
            gameClass: GameRace
    ) : GameSettingsSkillsAnalyticsEvent(UNSELECT_DEFAULT_RACE, game, Bundle().apply {
        putString(RACE_ID_PARAM, gameClass.id)
    })

    class DeleteCustomRace(
            val game: Game,
            gameClass: GameRace
    ) : GameSettingsSkillsAnalyticsEvent(DELETE_CUSTOM_RACE, game, Bundle().apply {
        putString(RACE_ID_PARAM, gameClass.id)
    })

    class UnselectCustomRace(
            val game: Game,
            gameClass: GameRace
    ) : GameSettingsSkillsAnalyticsEvent(UNSELECT_CUSTOM_RACE, game, Bundle().apply {
        putString(RACE_ID_PARAM, gameClass.id)
    })

    class UpdateRace(
            val game: Game,
            gameClass: GameRace
    ) : GameSettingsSkillsAnalyticsEvent(UPDATE_RACE, game, Bundle().apply {
        putString(RACE_ID_PARAM, gameClass.id)
        putString(DEFAULT_PARAM, GameRace.INFO.isSupported(gameClass).toString())
    })

}