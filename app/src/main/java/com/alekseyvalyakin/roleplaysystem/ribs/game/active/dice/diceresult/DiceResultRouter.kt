package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link DiceResultBuilder.DiceResultScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
class DiceResultRouter(
    view: DiceResultView,
    interactor: DiceResultInteractor,
    component: DiceResultBuilder.Component) : ViewRouter<DiceResultView, DiceResultInteractor, DiceResultBuilder.Component>(view, interactor, component)
