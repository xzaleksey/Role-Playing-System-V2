package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.races

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.races.UserGameRace
import com.alekseyvalyakin.roleplaysystem.views.backdrop.back.DefaultBackView
import com.alekseyvalyakin.roleplaysystem.views.backdrop.front.DefaultFrontView
import com.alekseyvalyakin.roleplaysystem.views.toolbar.CustomToolbarView

data class GameSettingsRaceViewModel(
        val toolBarModel: CustomToolbarView.Model,
        val frontModel: DefaultFrontView.Model,
        val backModel: DefaultBackView.Model,
        val step: Step,
        val selectedModel: UserGameRace? = null

) {

    enum class Step {
        COLLAPSED,
        EXPANDED
    }
}