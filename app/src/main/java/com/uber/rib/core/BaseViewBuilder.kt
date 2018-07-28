package com.uber.rib.core

import android.view.View
import android.view.ViewGroup

@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")
abstract class BaseViewBuilder<ViewType : View, RouterT : Router<*, *>, DependencyT>(
        dependency: DependencyT) : ViewBuilder<ViewType, RouterT, DependencyT>(dependency) {

    abstract fun build(parentViewGroup: ViewGroup): RouterT
}