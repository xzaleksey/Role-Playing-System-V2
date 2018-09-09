package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult

import android.content.Context
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getDoubleCommonDimen
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import com.alekseyvalyakin.roleplaysystem.utils.getSelectableItemBorderless
import com.alekseyvalyakin.roleplaysystem.utils.getString
import org.jetbrains.anko.*

class DiceResultDialogRethrowView(context: Context) : _LinearLayout(context) {

    lateinit var tvDiceCount: TextView
    lateinit var checkBox: CheckBox
    lateinit var diceContainer: LinearLayout
    var allContainer: RelativeLayout

    init {
        rightPadding = getDoubleCommonDimen()
        leftPadding = getDoubleCommonDimen()
        backgroundColor = getSelectableItemBorderless()
        orientation = LinearLayout.VERTICAL
        allContainer = relativeLayout {
            id = R.id.all_container
            backgroundColor = getSelectableItemBorderless()
            bottomPadding = getIntDimen(R.dimen.dp_12)
            topPadding = getIntDimen(R.dimen.dp_12)

            tvDiceCount = textView {
                id = R.id.tv_dice_count
                text = getString(R.string.all)
                textColorResource = R.color.colorTextPrimary
                textSizeDimen = R.dimen.dp_16
            }.lparams {
                centerVertically()
            }
            checkBox = checkBox {
                id = R.id.checkbox
            }.lparams {
                alignParentRight()
                centerVertically()
            }
        }.lparams(width = matchParent)
        diceContainer = linearLayout {
            id = R.id.dice_container
            orientation = LinearLayout.VERTICAL
        }.lparams(width = matchParent)

    }
}