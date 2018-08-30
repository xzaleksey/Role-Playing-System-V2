package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link ActiveGameBuilder.ActiveGameScope}.
 *
 */
class ActiveGameRouter(
    view: ActiveGameView,
    interactor: ActiveGameInteractor,
    component: ActiveGameBuilder.Component) : ViewRouter<ActiveGameView, ActiveGameInteractor, ActiveGameBuilder.Component>(view, interactor, component){


}
