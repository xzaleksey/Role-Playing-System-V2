package com.alekseyvalyakin.roleplaysystem.ribs.game.active.transition

import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameView
import com.uber.rib.core.BaseViewBuilder
import com.uber.rib.core.ViewRouter

class BaseActiveGameInternalAttachTransition<
        B : BaseViewBuilder<*, *>>(
        val builder: B,
        view: ActiveGameView
) : ActiveGameInternalAttachTransition(
        object : RouterCreator<ViewRouter<*, *, *>> {
            override fun createRouter(view: ViewGroup): ViewRouter<*, *, *> {
                return builder.build(view)
            }
        }, view)