package com.alekseyvalyakin.roleplaysystem.ribs.features

import android.content.Context
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getStatusBarHeight
import org.jetbrains.anko._LinearLayout
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.view

class FeaturesView constructor(context: Context) : _LinearLayout(context), FeaturesPresenter {

    init {
        view {
            backgroundColorResource = R.color.colorPrimaryDark
        }.lparams(width = matchParent, height = getStatusBarHeight())
    }
}
