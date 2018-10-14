package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.equip

import android.content.Context
import android.content.res.ColorStateList
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.*
import org.jetbrains.anko.*

class GameSettingsEquipView constructor(context: Context) : _LinearLayout(context), GameSettingsEquipPresenter {

    init {
        orientation = VERTICAL
        backgroundColorResource = R.color.backgroundColor
        isClickable = true

        view {
            backgroundColorResource = R.color.colorPrimaryDark
        }.lparams(width = matchParent, height = getStatusBarHeight())

        chipGroup {
            chip {
                id = 1
                setChipBackgroundColorResource(R.color.backgroundColor)
                setChipStrokeColorResource(R.color.colorAccent)
                textColorResource = R.color.colorAccent
                rippleColor = ColorStateList.valueOf(getCompatColor(R.color.colorAccent))
                highlightColor = getCompatColor(R.color.colorAccent)
                chipStrokeWidth = getFloatDimen(R.dimen.dp_1)
                text = "Test"
            }
        }.lparams(matchParent, wrapContent) {
            topMargin = getDoubleCommonDimen()
            leftMargin = getDoubleCommonDimen()
            rightMargin = getDoubleCommonDimen()
        }
    }
}
