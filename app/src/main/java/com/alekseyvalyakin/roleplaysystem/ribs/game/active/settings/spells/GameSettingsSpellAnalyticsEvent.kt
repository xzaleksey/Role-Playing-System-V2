package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.spells

import com.alekseyvalyakin.roleplaysystem.data.analytics.*
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.spells.GameSpell
import com.uber.rib.core.Bundle

sealed class GameSettingsSpellAnalyticsEvent(name: String, game: Game, bundle: Bundle = Bundle()) : GameAnalyticsEvent(name, game, bundle) {

    class CreateSpell(
            game: Game,
            gameClass: GameSpell
    ) : GameSettingsSpellAnalyticsEvent(CREATE_CUSTOM_SPELL, game, Bundle().apply {
        putString(NAME_PARAM, gameClass.getDisplayedName())
    })

    class SelectDefaultSpell(
            val game: Game,
            gameClass: GameSpell
    ) : GameSettingsSpellAnalyticsEvent(SELECT_DEFAULT_SPELL, game, Bundle().apply {
        putString(SPELL_ID_PARAM, gameClass.id)
    })

    class SelectCustomSpell(
            val game: Game,
            gameClass: GameSpell
    ) : GameSettingsSpellAnalyticsEvent(SELECT_DEFAULT_SPELL, game, Bundle().apply {
        putString(SPELL_ID_PARAM, gameClass.id)
    })

    class UnselectDefaultSpell(
            val game: Game,
            gameClass: GameSpell
    ) : GameSettingsSpellAnalyticsEvent(UNSELECT_DEFAULT_SPELL, game, Bundle().apply {
        putString(SPELL_ID_PARAM, gameClass.id)
    })

    class DeleteCustomSpell(
            val game: Game,
            gameClass: GameSpell
    ) : GameSettingsSpellAnalyticsEvent(DELETE_CUSTOM_SPELL, game, Bundle().apply {
        putString(SPELL_ID_PARAM, gameClass.id)
    })

    class UnselectCustomSpell(
            val game: Game,
            gameClass: GameSpell
    ) : GameSettingsSpellAnalyticsEvent(UNSELECT_CUSTOM_SPELL, game, Bundle().apply {
        putString(SPELL_ID_PARAM, gameClass.id)
    })

    class UpdateSpell(
            val game: Game,
            gameClass: GameSpell
    ) : GameSettingsSpellAnalyticsEvent(UPDATE_SPELL, game, Bundle().apply {
        putString(SPELL_ID_PARAM, gameClass.id)
        putString(DEFAULT_PARAM, GameSpell.INFO.isSupported(gameClass).toString())
    })

}