package com.alekseyvalyakin.roleplaysystem.flexible.twolineimage

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
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
import org.jetbrains.anko.bottomPadding
import org.jetbrains.anko.centerVertically
import org.jetbrains.anko.dip
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.imageView
import org.jetbrains.anko.leftOf
import org.jetbrains.anko.leftPadding
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.rightOf
import org.jetbrains.anko.rightPadding
import org.jetbrains.anko.textColor
import org.jetbrains.anko.textView
import org.jetbrains.anko.topPadding
import org.jetbrains.anko.wrapContent

class GameListView(
        context: Context
) : _RelativeLayout(context) {

    init {
        AnkoContext.createDelegate(this).apply {
            backgroundColor = Color.WHITE
            setForegroundSelectableItemBackGround()
            bottomPadding = getIntDimen(R.dimen.dp_12)
            rightPadding = getIntDimen(R.dimen.dp_16)
            leftPadding = getIntDimen(R.dimen.dp_16)
            topPadding = getIntDimen(R.dimen.dp_12)

            imageView {
                id = R.id.iv_right
                imageResource = R.drawable.ic_arrow_right
            }.lparams(width = getIntDimen(R.dimen.dp_24), height = getIntDimen(R.dimen.dp_24)) {
                alignParentRight()
                centerVertically()
                marginStart = getIntDimen(R.dimen.dp_8)
            }
            textView {
                id = R.id.primary_line
                ellipsize = TextUtils.TruncateAt.END
                maxLines = 1
                textColor = getCompatColor(R.color.colorTextPrimary)
                setTextSizeFromRes(R.dimen.sp_16)
            }.lparams(width = matchParent) {
                leftOf(R.id.iv_right)
            }
            imageView {
                id = R.id.icon
                visibility = View.GONE
                imageResource = R.drawable.crown
            }.lparams(width = getIntDimen(R.dimen.dp_18), height = getIntDimen(R.dimen.dp_18)) {
                below(R.id.primary_line)
                marginEnd = getIntDimen(R.dimen.dp_4)
                topMargin = dip(-2)
            }
            textView {
                id = R.id.secondary_line
                ellipsize = TextUtils.TruncateAt.END
                maxLines = 2
                textColor = getCompatColor(R.color.colorTextSecondary)
                setTextSizeFromRes(R.dimen.sp_14)
            }.lparams(width = matchParent) {
                below(R.id.primary_line)
                leftOf(R.id.iv_right)
                rightOf(R.id.icon)
            }
        }
        layoutParams = RecyclerView.LayoutParams(matchParent, wrapContent)
    }
}