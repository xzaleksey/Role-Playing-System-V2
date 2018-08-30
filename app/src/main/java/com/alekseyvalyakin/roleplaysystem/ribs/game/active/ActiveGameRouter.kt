package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import android.view.View

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link ActiveGameBuilder.ActiveGameScope}.
 *
 * TODO describe the possible child configurations of this scope.
 */
class ActiveGameRouter(
    view: ActiveGameView,
    interactor: ActiveGameInteractor,
    component: ActiveGameBuilder.Component) : ViewRouter<ActiveGameView, ActiveGameInteractor, ActiveGameBuilder.Component>(view, interactor, component)
