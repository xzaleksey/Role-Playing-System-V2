package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.log


import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link PhotoBuilder.PhotoScope}.
 *
 */
class LogRouter(
        view: LogView,
        interactor: LogInteractor,
        component: LogBuilder.Component) : ViewRouter<LogView, LogInteractor, LogBuilder.Component>(view, interactor, component)
