package com.alekseyvalyakin.roleplaysystem.views

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import com.alekseyvalyakin.roleplaysystem.utils.setSanserifMediumTypeface
import com.alekseyvalyakin.roleplaysystem.utils.setTextSizeFromRes
import org.jetbrains.anko.*

class SecondarySubheaderView(context: Context) : _FrameLayout(context) {
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
                gravity = Gravity.START
                setTextSizeFromRes(R.dimen.dp_14)
                setSanserifMediumTypeface()
                singleLine = true
            }.lparams {
                bottomMargin = getIntDimen(R.dimen.dp_4)
                rightMargin = getIntDimen(R.dimen.dp_16)
                leftMargin = getIntDimen(R.dimen.dp_16)
                topMargin = getIntDimen(R.dimen.dp_16)

                gravity = Gravity.START
            }

            view {
                id = R.id.divider
                backgroundResource = R.drawable.shadow_top_divider
                visibility = View.GONE
            }.lparams(width = matchParent, height = dip(3)) {
                gravity = Gravity.BOTTOM
            }
        }
        layoutParams = RecyclerView.LayoutParams(matchParent, wrapContent)
    }
}