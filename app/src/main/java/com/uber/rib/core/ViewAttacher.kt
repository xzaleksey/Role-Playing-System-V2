@file:Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")

package com.uber.rib.core

import android.view.View
import android.view.ViewGroup

interface ViewAttacher {
    fun addView(view: View)

    fun getViewGroup(): ViewGroup
}