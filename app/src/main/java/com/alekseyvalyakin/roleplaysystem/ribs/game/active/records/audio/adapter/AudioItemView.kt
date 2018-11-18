package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio.adapter

import android.content.Context
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getCommonDimen
import com.alekseyvalyakin.roleplaysystem.utils.getDoubleCommonDimen
import com.alekseyvalyakin.roleplaysystem.utils.getFloatDimen
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView

class AudioItemView(context: Context) : _FrameLayout(context) {

    private lateinit var tvText: TextView

    init {
        clipToPadding = false
        leftPadding = getDoubleCommonDimen()
        rightPadding = getDoubleCommonDimen()

        cardView {
            radius = getFloatDimen(R.dimen.dp_2)
            cardElevation = getFloatDimen(R.dimen.dp_4)
            tvText = textView {
                leftPadding = getDoubleCommonDimen()
                rightPadding = getDoubleCommonDimen()
                topPadding = getIntDimen(R.dimen.dp_12)
                bottomPadding = getIntDimen(R.dimen.dp_12)
                id = R.id.tv_text
                textColorResource = R.color.colorTextPrimary
            }.lparams(width = matchParent)
        }.lparams(width = matchParent) {
            bottomMargin = getCommonDimen()
        }
    }

    fun update(text: String) {
        tvText.text = text
    }

}