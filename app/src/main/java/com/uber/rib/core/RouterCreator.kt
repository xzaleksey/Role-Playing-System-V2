@file:Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")

package com.uber.rib.core

import android.view.ViewGroup

interface RouterCreator<R : ViewRouter<*, *, *>> {
    fun createRouter(view: ViewGroup): R
}