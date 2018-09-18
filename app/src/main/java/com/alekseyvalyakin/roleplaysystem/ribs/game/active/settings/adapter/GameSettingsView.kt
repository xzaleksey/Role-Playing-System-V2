package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.adapter

import android.content.Context
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getDoubleCommonDimen
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import com.alekseyvalyakin.roleplaysystem.utils.getSelectableItemBackGround
import org.jetbrains.anko.*

class GameSettingsView(context: Context) : _RelativeLayout(context) {

    private var tvText: TextView

    init {
        backgroundResource = getSelectableItemBackGround()
        topPadding = getIntDimen(R.dimen.dp_12)
        bottomPadding = getIntDimen(R.dimen.dp_12)
        leftPadding = getDoubleCommonDimen()
        rightPadding = getDoubleCommonDimen()

        imageView {
            id = R.id.arrow_right
            imageResource = R.drawable.ic_keyboard_arrow_right
        }.lparams(getIntDimen(R.dimen.dp_24), getIntDimen(R.dimen.dp_24)) {
            alignParentEnd()
            centerVertically()
        }

        imageView {
            id = R.id.left_icon
            imageResource = R.drawable.ic_delete_black_24dp
        }.lparams(getIntDimen(R.dimen.dp_24), getIntDimen(R.dimen.dp_24)) {
            centerVertically()
        }

        tvText = textView {
            textSizeDimen = R.dimen.dp_16
        }.lparams(width = matchParent, height = wrapContent) {
            endOf(R.id.left_icon)
            startOf(R.id.arrow_right)
            centerVertically()
            leftMargin = getDoubleCommonDimen()
            rightMargin = getDoubleCommonDimen()
        }
    }

    fun update(title: String, onClickListener: OnClickListener) {
        setOnClickListener(onClickListener)
        tvText.text = title
    }

}