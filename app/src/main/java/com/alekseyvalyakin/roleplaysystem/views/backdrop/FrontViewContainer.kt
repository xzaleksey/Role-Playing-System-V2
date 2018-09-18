package com.alekseyvalyakin.roleplaysystem.views.backdrop

import android.view.View
import org.jetbrains.anko.matchParent

class FrontViewContainer<T : View>(
        view: T,
        width: Int = matchParent,
        height: Int = matchParent
) : BaseViewContainer<T>(view, width, height) {

}