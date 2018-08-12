package com.alekseyvalyakin.roleplaysystem.ribs.profile.transition

import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.User
import com.alekseyvalyakin.roleplaysystem.ribs.profile.ProfileBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.profile.ProfileRouter
import com.uber.rib.core.RouterNavigator
import com.uber.rib.core.RouterNavigatorState

class ProfileAttachTransition<S : RouterNavigatorState>(
        val builder: ProfileBuilder,
        val view: ViewGroup,
        val user: User
) : RouterNavigator.AttachTransition<ProfileRouter, S> {

    override fun buildRouter(): ProfileRouter {
        return builder.build(view, user)
    }

    override fun willAttachToHost(router: ProfileRouter, previousState: S?, newState: S, isPush: Boolean) {
        view.addView(router.view)
    }

}