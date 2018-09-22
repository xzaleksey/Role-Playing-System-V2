package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats

import com.alekseyvalyakin.roleplaysystem.views.backdrop.back.DefaultBackView
import com.alekseyvalyakin.roleplaysystem.views.backdrop.front.DefaultFrontView
import com.alekseyvalyakin.roleplaysystem.views.toolbar.CustomToolbarView

data class GameSettingsStatViewModel(
        val toolBarModel: CustomToolbarView.Model,
        val frontModel: DefaultFrontView.Model,
        val backModel: DefaultBackView.Model,
        val step: Step
) {

    enum class Step {
        NEW_STAT,
        SHOW_ALL
    }
}