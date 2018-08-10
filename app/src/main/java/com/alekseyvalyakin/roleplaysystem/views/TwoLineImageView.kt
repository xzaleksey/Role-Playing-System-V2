package com.alekseyvalyakin.roleplaysystem.views

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getCompatColor
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import com.alekseyvalyakin.roleplaysystem.utils.setForegroundSelectableItemBackGround
import com.alekseyvalyakin.roleplaysystem.utils.setTextSizeFromRes
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko._RelativeLayout
import org.jetbrains.anko.alignParentRight
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.below
import org.jetbrains.anko.centerVertically
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.imageView
import org.jetbrains.anko.leftOf
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.rightPadding
import org.jetbrains.anko.textColor
import org.jetbrains.anko.textView
import org.jetbrains.anko.topPadding
import org.jetbrains.anko.wrapContent

class TwoLineImageView(context: Context) : _RelativeLayout(context) {

    init {
        AnkoContext.createDelegate(this).apply {
            backgroundColor = Color.WHITE
            setForegroundSelectableItemBackGround()
            minimumHeight = getIntDimen(R.dimen.dp_72)
            rightPadding = getIntDimen(R.dimen.dp_16)
            topPadding = getIntDimen(R.dimen.dp_16)
            imageView {
                id = R.id.avatar
            }.lparams(width = getIntDimen(R.dimen.dp_40), height = getIntDimen(R.dimen.dp_40)) {
                leftMargin = getIntDimen(R.dimen.dp_16)
                marginStart = getIntDimen(R.dimen.dp_16)
            }
            relativeLayout {
                imageView {
                    id = R.id.arrow_right
                    visibility = View.GONE
                    imageResource = R.drawable.ic_keyboard_arrow_right
                }.lparams(width = getIntDimen(R.dimen.dp_24), height = getIntDimen(R.dimen.dp_24)) {
                    alignParentRight()
                    centerVertically()
                }
                textView {
                    id = R.id.primary_line
                    maxLines = 1
                    textColor = getCompatColor(R.color.colorTextPrimary)
                    setTextSizeFromRes(R.dimen.sp_16)
                }.lparams(width = matchParent) {
                    leftOf(R.id.arrow_right)
                }
                textView {
                    id = R.id.secondary_line
                    maxLines = 1
                    textColor = getCompatColor(R.color.colorTextSecondary)
                    setTextSizeFromRes(R.dimen.sp_14)
                }.lparams(width = matchParent) {
                    below(R.id.primary_line)
                    leftOf(R.id.arrow_right)
                }
            }.lparams(width = matchParent) {
                centerVertically()
                leftMargin = getIntDimen(R.dimen.dp_72)
                marginStart = getIntDimen(R.dimen.dp_72)
            }
        }

        layoutParams = RecyclerView.LayoutParams(matchParent, wrapContent)
    }
}