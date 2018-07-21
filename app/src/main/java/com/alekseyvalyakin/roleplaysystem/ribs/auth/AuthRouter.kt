package com.alekseyvalyakin.roleplaysystem.ribs.auth

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link AuthBuilder.AuthScope}.
 *
 */
class AuthRouter(
    view: AuthView,
    interactor: AuthInteractor,
    component: AuthBuilder.Component) : ViewRouter<AuthView, AuthInteractor, AuthBuilder.Component>(view, interactor, component)
