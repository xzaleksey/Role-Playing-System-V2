package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills

import android.content.Context
import com.alekseyvalyakin.roleplaysystem.R
import org.jetbrains.anko._RelativeLayout
import org.jetbrains.anko.backgroundResource

/**
 * Top level view for {@link GameSettingsSkillsBuilder.GameSettingsSkillsScope}.
 */
class GameSettingsSkillsView constructor(context: Context) : _RelativeLayout(context), GameSettingsSkillsPresenter {

    init {
        backgroundResource = R.color.colorBlack
    }
}
