package com.alekseyvalyakin.roleplaysystem.views.backdrop.front

import android.view.View
import com.alekseyvalyakin.roleplaysystem.views.backdrop.BaseViewContainer
import org.jetbrains.anko.matchParent

class FrontViewContainer<T : View>(
        view: T,
        width: Int = matchParent,
        height: Int = matchParent
) : BaseViewContainer<T>(view, width, height) {

}