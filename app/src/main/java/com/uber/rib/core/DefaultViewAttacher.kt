package com.uber.rib.core

import android.view.View
import android.view.ViewGroup

open class DefaultViewAttacher(
        private val parentView: ViewGroup
) : ViewAttacher {
    override fun getViewGroup(): ViewGroup {
        return parentView
    }

    override fun addView(view: View) {
        parentView.addView(view)
    }
}