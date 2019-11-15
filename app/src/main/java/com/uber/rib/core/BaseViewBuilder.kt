package com.uber.rib.core

import android.view.View
import android.view.ViewGroup

abstract class BaseViewBuilder<ViewType : View, DependencyT>(
        dependency: DependencyT) : ViewBuilder<ViewType, ViewRouter<*, *, *>, DependencyT>(dependency) {

    abstract fun build(parentViewGroup: ViewGroup): ViewRouter<*, *, *>
}