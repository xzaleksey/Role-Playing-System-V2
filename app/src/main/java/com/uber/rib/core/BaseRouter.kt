package com.uber.rib.core

import android.view.View

@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")
open class BaseRouter<V : View, I : Interactor<*, out Router<I, C>>, C : InteractorBaseComponent<I>>(view: V, interactor: I, component: C) : ViewRouter<V, I, C>(view, interactor, component) {

}