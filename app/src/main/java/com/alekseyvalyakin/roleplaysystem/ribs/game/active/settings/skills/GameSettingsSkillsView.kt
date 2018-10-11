package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills

import android.content.Context
import android.widget.EditText
import com.alekseyvalyakin.roleplaysystem.R
import org.jetbrains.anko._LinearLayout
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.editText

/**
 * Top level view for {@link GameSettingsSkillsBuilder.GameSettingsSkillsScope}.
 */
class GameSettingsSkillsView constructor(context: Context) : _LinearLayout(context), GameSettingsSkillsPresenter {

    private lateinit var etName: EditText
    private lateinit var etDescription: EditText

    init {
        orientation = VERTICAL
        backgroundResource = R.color.colorBlack

        etName = editText {

        }
        etDescription = editText {

        }
    }
}
