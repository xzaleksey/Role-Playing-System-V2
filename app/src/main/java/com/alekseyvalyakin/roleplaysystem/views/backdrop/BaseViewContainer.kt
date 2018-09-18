package com.alekseyvalyakin.roleplaysystem.views.backdrop

import android.view.View
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent

open class BaseViewContainer<T:View>(
        val view: T,
        val width: Int = matchParent,
        val height: Int = wrapContent
)