@file:Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")

package com.alekseyvalyakin.roleplaysystem.ribs.game.active.transition

import android.view.ViewGroup
import com.uber.rib.core.ViewRouter

interface RouterCreator<R : ViewRouter<*, *, *>> {
    fun createRouter(view: ViewGroup): R
}