package com.alekseyvalyakin.roleplaysystem.views.backdrop.front

import android.view.View
import com.alekseyvalyakin.roleplaysystem.views.backdrop.BaseViewContainer
import org.jetbrains.anko.matchParent

class FrontViewContainer<T>(
        view: T,
        width: Int = matchParent,
        height: Int = matchParent
) : BaseViewContainer<T>(view, width, height) where T : View, T : FrontView {

    init {
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        view.setOnClickListener { }
    }

    fun getHeaderHeight() = view.getHeaderHeight()

}