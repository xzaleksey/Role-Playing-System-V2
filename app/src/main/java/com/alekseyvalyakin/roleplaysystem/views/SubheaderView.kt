package com.alekseyvalyakin.roleplaysystem.views

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import com.alekseyvalyakin.roleplaysystem.utils.setTextSizeFromRes
import org.jetbrains.anko.*

class SubheaderView(context: Context) : _FrameLayout(context) {
    init {
        AnkoContext.createDelegate(this).apply {

            view {
                id = R.id.top_divider
                backgroundResource = R.drawable.shadow_bottom_divider
                visibility = View.GONE
            }.lparams(width = matchParent, height = dip(3)) {
                gravity = Gravity.TOP
            }
            textView {
                id = R.id.text
                maxLines = 1
                setTextSizeFromRes(R.dimen.sp_14)
                typeface = Typeface.create("sans-serif-medium",Typeface.NORMAL)
                singleLine = true
            }.lparams {
                leftMargin = getIntDimen(R.dimen.dp_16)
                bottomMargin = getIntDimen(R.dimen.dp_8)
                rightMargin = getIntDimen(R.dimen.dp_16)
                topMargin = getIntDimen(R.dimen.dp_16)
            }
            view {
                id = R.id.divider
                backgroundResource = R.drawable.shadow_top_divider
                visibility = View.GONE
            }.lparams(width = matchParent, height = dip(3)) {
                gravity = Gravity.BOTTOM
            }
        }
    }
}