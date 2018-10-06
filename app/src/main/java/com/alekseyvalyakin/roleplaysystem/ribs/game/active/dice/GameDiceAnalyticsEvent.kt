package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice

import com.alekseyvalyakin.roleplaysystem.data.analytics.*
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.uber.rib.core.Bundle

sealed class GameDiceAnalyticsEvent(name: String, game: Game, bundle: Bundle = Bundle()) : GameAnalyticsEvent(name, game, bundle) {

    class ThrowDice(
            game: Game
    ) : GameDiceAnalyticsEvent(THROW_DICE, game)

    class CancelButtonCLick(
            game: Game
    ) : GameDiceAnalyticsEvent(CANCEL_BUTTON_CLICK, game)

    class RethrowAllDices(
            game: Game
    ) : GameDiceAnalyticsEvent(RETHROW_ALL_DICES, game)

    class RethrowDices(
            game: Game
    ) : GameDiceAnalyticsEvent(RETHROW_DICES, game)

    class DeleteGameCollection(
            game: Game
    ) : GameDiceAnalyticsEvent(DELETE_DICE_COLLECTION, game)

    class CreateDiceCollection(
            game: Game
    ) : GameDiceAnalyticsEvent(CREATE_DICE_COLLECTION, game)

    class SelectDiceCollection(
            game: Game
    ) : GameDiceAnalyticsEvent(SELECT_DICE_COLLECTION, game)

    class UnselectDiceCollection(
            game: Game
    ) : GameDiceAnalyticsEvent(UNSELECT_DICE_COLLECTION, game)
}