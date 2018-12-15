package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills.UserGameSkill
import com.alekseyvalyakin.roleplaysystem.views.backdrop.front.DefaultFrontView
import com.alekseyvalyakin.roleplaysystem.views.toolbar.CustomToolbarView

data class GameSettingsSkillViewModel(
        val toolBarModel: CustomToolbarView.Model,
        val frontModel: DefaultFrontView.Model,
        val backModel: SkillBackView.Model,
        val step: Step,
        val selectedModel: UserGameSkill? = null

) {

    enum class Step {
        COLLAPSED,
        EXPANDED
    }
}