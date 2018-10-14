package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills

import com.alekseyvalyakin.roleplaysystem.data.analytics.*
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills.GameSkill
import com.uber.rib.core.Bundle

sealed class GameSettingsSkillAnalyticsEvent(name: String, game: Game, bundle: Bundle = Bundle()) : GameAnalyticsEvent(name, game, bundle) {

    class CreateSkill(
            game: Game,
            gameClass: GameSkill
    ) : GameSettingsSkillAnalyticsEvent(CREATE_CUSTOM_SKILL, game, Bundle().apply {
        putString(NAME_PARAM, gameClass.getDisplayedName())
    })

    class SelectDefaultSkill(
            val game: Game,
            gameClass: GameSkill
    ) : GameSettingsSkillAnalyticsEvent(SELECT_DEFAULT_SKILL, game, Bundle().apply {
        putString(SKILL_ID_PARAM, gameClass.id)
    })

    class SelectCustomSkill(
            val game: Game,
            gameClass: GameSkill
    ) : GameSettingsSkillAnalyticsEvent(SELECT_DEFAULT_SKILL, game, Bundle().apply {
        putString(SKILL_ID_PARAM, gameClass.id)
    })

    class UnselectDefaultSkill(
            val game: Game,
            gameClass: GameSkill
    ) : GameSettingsSkillAnalyticsEvent(UNSELECT_DEFAULT_SKILL, game, Bundle().apply {
        putString(SKILL_ID_PARAM, gameClass.id)
    })

    class DeleteCustomSkill(
            val game: Game,
            gameClass: GameSkill
    ) : GameSettingsSkillAnalyticsEvent(DELETE_CUSTOM_SKILL, game, Bundle().apply {
        putString(SKILL_ID_PARAM, gameClass.id)
    })

    class UnselectCustomSkill(
            val game: Game,
            gameClass: GameSkill
    ) : GameSettingsSkillAnalyticsEvent(UNSELECT_CUSTOM_SKILL, game, Bundle().apply {
        putString(SKILL_ID_PARAM, gameClass.id)
    })

    class UpdateSkill(
            val game: Game,
            gameClass: GameSkill
    ) : GameSettingsSkillAnalyticsEvent(UPDATE_SKILL, game, Bundle().apply {
        putString(SKILL_ID_PARAM, gameClass.id)
        putString(DEFAULT_PARAM, GameSkill.INFO.isSupported(gameClass).toString())
    })

}