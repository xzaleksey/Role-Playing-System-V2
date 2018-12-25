package com.alekseyvalyakin.roleplaysystem.ribs.profile

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link ProfileBuilder.ProfileScope}.
 *
 */
class ProfileRouter(
        view: ProfileView,
        interactor: ProfileInteractor,
        component: ProfileBuilder.Component
) : ViewRouter<ProfileView, ProfileInteractor, ProfileBuilder.Component>(view, interactor, component)
