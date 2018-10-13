package com.alekseyvalyakin.roleplaysystem.ribs.game.active.transition

import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameView
import com.uber.rib.core.BaseViewBuilder
import com.uber.rib.core.ViewRouter

@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")
class BaseActiveGameInternalAttachTransition<R : ViewRouter<*, *, *>,
        B : BaseViewBuilder<*, R, *>>(
        val builder: B,
        view: ActiveGameView
) : ActiveGameInternalAttachTransition<R>(
        object : RouterCreator<R> {
            override fun createRouter(view: ViewGroup): R {
                return builder.build(view)
            }
        }, view)