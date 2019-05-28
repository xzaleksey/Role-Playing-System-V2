@file:Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")

package com.uber.rib.core

import android.view.ViewGroup

class DefaultRouterCreator<R : ViewRouter<*, *, *>, B : BaseViewBuilder<*, R, *>>(
        val builder: B
) : RouterCreator<R> {
    override fun createRouter(view: ViewGroup): R {
        return builder.build(view)
    }
}