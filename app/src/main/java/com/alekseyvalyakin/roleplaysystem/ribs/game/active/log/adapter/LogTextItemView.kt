package com.alekseyvalyakin.roleplaysystem.ribs.game.active.log.adapter

import android.content.Context
import android.view.Gravity
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getCommonDimen
import com.alekseyvalyakin.roleplaysystem.utils.getDoubleCommonDimen
import org.jetbrains.anko.*

class LogTextItemView(context: Context) : _RelativeLayout(context) {

    private lateinit var tvTime: TextView
    private lateinit var tvText: TextView

    init {
        backgroundColorResource = R.color.colorWhite
        relativeLayout {
            minimumHeight = dimen(R.dimen.dp_56)
            bottomPadding = getCommonDimen()
            leftPadding = getDoubleCommonDimen()
            rightPadding = getDoubleCommonDimen()
            topPadding = getCommonDimen()

            tvTime = textView {
                id = R.id.tv_time
                gravity = Gravity.BOTTOM
                textColorResource = R.color.colorTextSecondary
            }.lparams {
                alignParentRight()
                leftMargin = getCommonDimen()
            }
            tvText = textView {
                id = R.id.tv_text
                textColorResource = R.color.colorTextPrimary
            }.lparams(width = matchParent) {
                centerVertically()
                leftOf(R.id.tv_time)
            }
        }.lparams(width = matchParent) {
        }
    }

    fun update(text: String, time: String) {
        tvText.text = text
        tvTime.text = time
    }

}