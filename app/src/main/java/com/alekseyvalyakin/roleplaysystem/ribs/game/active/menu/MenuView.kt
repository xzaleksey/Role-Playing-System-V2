package com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu

import android.content.Context
import com.alekseyvalyakin.roleplaysystem.R
import org.jetbrains.anko._LinearLayout
import org.jetbrains.anko.backgroundColorResource

class MenuView constructor(
        context: Context
) : _LinearLayout(context), MenuPresenter {

    init {
        orientation = VERTICAL
        backgroundColorResource = R.color.blackColor54
    }
}
