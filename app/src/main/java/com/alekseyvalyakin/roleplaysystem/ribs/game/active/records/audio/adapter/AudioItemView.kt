package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.*
import org.jetbrains.anko.*

class AudioItemView(context: Context) : _FrameLayout(context) {

    private lateinit var tvText: TextView
    private lateinit var tvSecondary: TextView
    private lateinit var ivIcon: ImageView

    init {
        backgroundColorResource = R.color.colorWhite

        relativeLayout {

            leftPadding = getDoubleCommonDimen()
            rightPadding = getDoubleCommonDimen()
            topPadding = getIntDimen(R.dimen.dp_10)
            bottomPadding = getIntDimen(R.dimen.dp_10)
            backgroundResource = getSelectableItemBackGround()

            ivIcon = imageView {
                imageResource = R.drawable.ic_play
                id = R.id.icon
            }.lparams(getIntDimen(R.dimen.dp_24), getIntDimen(R.dimen.dp_24)) {
                alignParentEnd()
                centerVertically()
            }

            tvText = textView {
                id = R.id.tv_text
                textColorResource = R.color.colorTextPrimary
                body1Style()
            }.lparams(width = matchParent) {
                alignParentLeft()
                leftOf(R.id.icon)
            }

            tvSecondary = textView {
                textColorResource = R.color.colorTextSecondary
                text = "Test"
                captionStyle()
            }.lparams(width = matchParent) {
                alignParentLeft()
                leftOf(R.id.icon)
                below(R.id.tv_text)
            }
        }.lparams(matchParent)
    }

    fun update(audioItemViewModel: AudioItemViewModel) {
        tvText.text = audioItemViewModel.text
    }

}