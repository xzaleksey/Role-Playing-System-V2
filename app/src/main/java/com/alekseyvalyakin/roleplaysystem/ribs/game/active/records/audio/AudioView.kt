package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio


import android.content.Context
import com.alekseyvalyakin.roleplaysystem.R
import org.jetbrains.anko._LinearLayout
import org.jetbrains.anko.backgroundColorResource

class AudioView constructor(
        context: Context
) : _LinearLayout(context), AudioPresenter {

    init {
        backgroundColorResource = R.color.blackColor54
    }
}
