package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice

import android.view.View

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link DiceBuilder.DiceScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
class DiceRouter(
    view: DiceView,
    interactor: DiceInteractor,
    component: DiceBuilder.Component) : ViewRouter<DiceView, DiceInteractor, DiceBuilder.Component>(view, interactor, component)
