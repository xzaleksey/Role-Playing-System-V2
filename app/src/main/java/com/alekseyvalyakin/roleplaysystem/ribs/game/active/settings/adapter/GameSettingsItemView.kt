package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getDoubleCommonDimen
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import com.alekseyvalyakin.roleplaysystem.utils.getSelectableItemBackGround
import com.alekseyvalyakin.roleplaysystem.utils.tintImageRes
import org.jetbrains.anko.*

class GameSettingsItemView(context: Context) : _FrameLayout(context) {

    private lateinit var tvText: TextView
    private lateinit var ivLeft: ImageView

    init {
        backgroundResource = getSelectableItemBackGround()
        relativeLayout {
            backgroundColorResource = R.color.colorWhite
            topPadding = getIntDimen(R.dimen.dp_12)
            bottomPadding = getIntDimen(R.dimen.dp_12)
            leftPadding = getDoubleCommonDimen()
            rightPadding = getDoubleCommonDimen()

            imageView {
                id = R.id.arrow_right
                imageResource = R.drawable.ic_arrow_right
            }.lparams(getIntDimen(R.dimen.dp_24), getIntDimen(R.dimen.dp_24)) {
                alignParentEnd()
                centerVertically()
            }

            ivLeft = imageView {
                id = R.id.left_icon
                tintImageRes(R.color.colorPrimary)
            }.lparams(getIntDimen(R.dimen.dp_24), getIntDimen(R.dimen.dp_24)) {
                centerVertically()
            }

            tvText = textView {
                textSizeDimen = R.dimen.dp_16
                textColorResource = R.color.colorTextPrimary
            }.lparams(width = matchParent, height = wrapContent) {
                endOf(R.id.left_icon)
                startOf(R.id.arrow_right)
                centerVertically()
                leftMargin = getDoubleCommonDimen()
                rightMargin = getDoubleCommonDimen()
            }
        }.lparams(matchParent)
    }

    fun update(title: String, leftIcon: Drawable, onClickListener: OnClickListener) {
        setOnClickListener(onClickListener)
        tvText.text = title
        ivLeft.setImageDrawable(leftIcon)
    }

}