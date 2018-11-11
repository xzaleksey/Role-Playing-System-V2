package com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link PhotoBuilder.PhotoScope}.
 *
 */
class MenuRouter(
        view: MenuView,
        interactor: MenuInteractor,
        component: MenuBuilder.Component) : ViewRouter<MenuView, MenuInteractor, MenuBuilder.Component>(view, interactor, component)
