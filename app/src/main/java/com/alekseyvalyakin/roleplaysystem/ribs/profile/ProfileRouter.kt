package com.alekseyvalyakin.roleplaysystem.ribs.profile

import com.alekseyvalyakin.roleplaysystem.ribs.profile.provider.ProfileUserProvider
import com.uber.rib.core.RestorableRouter
import com.uber.rib.core.ViewRouter
import java.io.Serializable

/**
 * Adds and removes children of {@link ProfileBuilder.ProfileScope}.
 *
 */
class ProfileRouter(
        view: ProfileView,
        interactor: ProfileInteractor,
        component: ProfileBuilder.Component,
        private val profileUserProvider: ProfileUserProvider) : ViewRouter<ProfileView, ProfileInteractor, ProfileBuilder.Component>(view, interactor, component),
        RestorableRouter {

    override fun getRestorableInfo(): Serializable? {
        return profileUserProvider.getCurrentUser()
    }
}
