package com.alekseyvalyakin.roleplaysystem.views.backdrop

import android.view.View
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

class BackViewContainer<T : View>(
        view: T,
        width: Int = matchParent,
        height: Int = wrapContent
) : BaseViewContainer<T>(view, width, height) {

    fun getPeekHeightDif(): Int {
        return view.measuredHeight
    }
}