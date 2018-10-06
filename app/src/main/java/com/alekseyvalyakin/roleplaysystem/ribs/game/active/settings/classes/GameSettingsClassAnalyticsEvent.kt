package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes

import com.alekseyvalyakin.roleplaysystem.data.analytics.*
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes.GameClass
import com.uber.rib.core.Bundle

sealed class GameSettingsClassAnalyticsEvent(name: String, game: Game, bundle: Bundle = Bundle()) : GameAnalyticsEvent(name, game, bundle) {

    class CreateClass(
            game: Game,
            gameClass: GameClass
    ) : GameSettingsClassAnalyticsEvent(CREATE_CUSTOM_CLASS, game, Bundle().apply {
        putString(NAME_PARAM, gameClass.getDisplayedName())
    })

    class SelectDefaultClass(
            val game: Game,
            gameClass: GameClass
    ) : GameSettingsClassAnalyticsEvent(SELECT_DEFAULT_CLASS, game, Bundle().apply {
        putString(CLASS_ID_PARAM, gameClass.id)
    })

    class SelectCustomClass(
            val game: Game,
            gameClass: GameClass
    ) : GameSettingsClassAnalyticsEvent(SELECT_DEFAULT_CLASS, game, Bundle().apply {
        putString(CLASS_ID_PARAM, gameClass.id)
    })

    class UnselectDefaultClass(
            val game: Game,
            gameClass: GameClass
    ) : GameSettingsClassAnalyticsEvent(UNSELECT_DEFAULT_CLASS, game, Bundle().apply {
        putString(CLASS_ID_PARAM, gameClass.id)
    })

    class DeleteCustomClass(
            val game: Game,
            gameClass: GameClass
    ) : GameSettingsClassAnalyticsEvent(DELETE_CUSTOM_CLASS, game, Bundle().apply {
        putString(CLASS_ID_PARAM, gameClass.id)
    })

    class UnselectCustomClass(
            val game: Game,
            gameClass: GameClass
    ) : GameSettingsClassAnalyticsEvent(UNSELECT_CUSTOM_CLASS, game, Bundle().apply {
        putString(CLASS_ID_PARAM, gameClass.id)
    })

    class UpdateClass(
            val game: Game,
            gameClass: GameClass
    ) : GameSettingsClassAnalyticsEvent(UPDATE_CLASS, game, Bundle().apply {
        putString(CLASS_ID_PARAM, gameClass.id)
        putString(DEFAULT_PARAM, GameClass.INFO.isSupported(gameClass).toString())
    })

}