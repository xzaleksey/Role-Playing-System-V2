package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes.UserGameClass
import com.alekseyvalyakin.roleplaysystem.views.backdrop.back.DefaultBackView
import com.alekseyvalyakin.roleplaysystem.views.backdrop.front.DefaultFrontView
import com.alekseyvalyakin.roleplaysystem.views.toolbar.CustomToolbarView

data class GameSettingsClassViewModel(
        val toolBarModel: CustomToolbarView.Model,
        val frontModel: DefaultFrontView.Model,
        val backModel: DefaultBackView.Model,
        val step: Step,
        val selectedModel: UserGameClass? = null

) {

    enum class Step {
        COLLAPSED,
        EXPANDED
    }
}