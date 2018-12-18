package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult

import android.content.Context
import android.view.Gravity
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import com.alekseyvalyakin.roleplaysystem.utils.getSelectableItemBorderless
import com.alekseyvalyakin.roleplaysystem.utils.tintImageRes
import org.jetbrains.anko.*

class DiceResultDialogRethrowItemView(context: Context) : _RelativeLayout(context) {


    lateinit var ivMainDice: ImageView

    lateinit var tvDiceCount: TextView

    lateinit var diceCheckBox: CheckBox

    lateinit var tvMainResult: TextView

    init {
        relativeLayout {
            backgroundResource = getSelectableItemBorderless()
            bottomPadding = getIntDimen(R.dimen.dp_12)
            topPadding = getIntDimen(R.dimen.dp_12)
            ivMainDice = imageView {
                id = R.id.iv_main_dice
                tintImageRes(R.color.colorDiceResult)
            }.lparams(width = getIntDimen(R.dimen.dp_20), height = getIntDimen(R.dimen.dp_20)) {
                centerVertically()
                leftMargin = getIntDimen(R.dimen.dp_2)
                marginStart = getIntDimen(R.dimen.dp_2)
            }
            tvDiceCount = textView {
                id = R.id.tv_choose_all
                gravity = Gravity.CENTER
                textColorResource = R.color.colorTextPrimary
                textSizeDimen = R.dimen.dp_16
            }.lparams {
                centerVertically()
                leftMargin = getIntDimen(R.dimen.dp_18)
                rightMargin = getIntDimen(R.dimen.dp_4)
                marginStart = getIntDimen(R.dimen.dp_18)
                rightOf(R.id.iv_main_dice)
            }
            diceCheckBox = checkBox {
                id = R.id.checkbox
            }.lparams {
                alignParentRight()
                centerVertically()
            }
            tvMainResult = textView {
                id = R.id.tv_main_result
                gravity = Gravity.CENTER
                textColorResource = R.color.colorAccent
                textSizeDimen = R.dimen.dp_16
            }.lparams {
                centerVertically()
                leftOf(R.id.checkbox)
                rightOf(R.id.tv_choose_all)
            }
        }

    }
}