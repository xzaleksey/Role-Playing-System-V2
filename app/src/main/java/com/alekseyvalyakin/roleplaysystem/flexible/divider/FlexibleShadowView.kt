package com.alekseyvalyakin.roleplaysystem.flexible.divider

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko._FrameLayout
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.dip
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.view

class FlexibleShadowView(
        context: Context
) : _FrameLayout(context) {

    init {
        AnkoContext.createDelegate(this).apply {
            view {
                backgroundResource = R.drawable.shadow_bottom_divider
            }.lparams(matchParent, dip(3)) {
                gravity = Gravity.TOP
            }
        }
        layoutParams = RecyclerView.LayoutParams(matchParent, getIntDimen(R.dimen.dp_8))
    }

}