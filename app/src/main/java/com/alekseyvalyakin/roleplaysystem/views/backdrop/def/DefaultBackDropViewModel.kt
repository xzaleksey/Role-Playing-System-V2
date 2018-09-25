package com.alekseyvalyakin.roleplaysystem.views.backdrop.def

import com.alekseyvalyakin.roleplaysystem.views.backdrop.back.DefaultBackView
import com.alekseyvalyakin.roleplaysystem.views.backdrop.front.DefaultFrontView
import com.alekseyvalyakin.roleplaysystem.views.toolbar.CustomToolbarView

data class DefaultBackDropViewModel(
        val toolBarModel: CustomToolbarView.Model,
        val frontModel: DefaultFrontView.Model,
        val backModel: DefaultBackView.Model,
        val step: Step
) {

    enum class Step {
        COLLAPSED,
        EXPANDED
    }
}