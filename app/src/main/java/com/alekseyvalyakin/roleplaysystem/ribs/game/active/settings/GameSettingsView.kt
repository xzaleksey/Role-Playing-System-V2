package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings

import android.content.Context
import android.widget.LinearLayout
import org.jetbrains.anko._LinearLayout

/**
 * Top level view for {@link GameSettingsBuilder.GameSettingsScope}.
 */
class GameSettingsView constructor(context: Context) : _LinearLayout(context), GameSettingsInteractor.GameSettingsPresenter {

    init {
        orientation = LinearLayout.VERTICAL


    }
}
