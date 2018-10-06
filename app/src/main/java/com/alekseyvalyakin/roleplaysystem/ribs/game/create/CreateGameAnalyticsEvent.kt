package com.alekseyvalyakin.roleplaysystem.ribs.game.create

import com.alekseyvalyakin.roleplaysystem.data.analytics.*
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.uber.rib.core.Bundle

sealed class CreateGameAnalyticsEvent(name: String, game: Game, bundle: Bundle = Bundle()) : GameAnalyticsEvent(name, game, bundle) {

    class ClickNext(
            game: Game,
            step: CreateGameStep,
            nextStep: CreateGameStep
    ) : CreateGameAnalyticsEvent(CLICK_NEXT, game, Bundle().apply {
        putString(CURRENT_STEP_PARAM, step.text)
        putString(NEXT_STEP_PARAM, nextStep.text)
    })

    class ClickBack(
            game: Game,
            step: CreateGameStep,
            previousStep: CreateGameStep
    ) : CreateGameAnalyticsEvent(CLICK_BACK, game, Bundle().apply {
        putString(CURRENT_STEP_PARAM, step.text)
        putString(PREVIOUS_STEP_PARAM, previousStep.text)
    })

    class ActivateGame(
            game: Game
    ) : CreateGameAnalyticsEvent(ACTIVATE_GAME, game)

    class DeleteGame(
            game: Game
    ) : CreateGameAnalyticsEvent(DELETE_GAME, game)

}