package com.alekseyvalyakin.roleplaysystem.utils

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.support.annotation.*
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.ImageViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.ribs.main.FabEnabledProvider

fun TextView.setTextSizeFromRes(@DimenRes res: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getFloatDimen(res))
}

fun View.getFloatDimen(@DimenRes res: Int): Float {
    return context.getFloatDimen(res)
}

fun View.getIntDimen(@DimenRes res: Int): Int {
    return context.getIntDimen(res)
}

fun View.getCommonDimen(): Int {
    return context.getCommonDimen()
}

fun View.getDoubleCommonDimen(): Int {
    return context.getDoubleCommonDimen()
}

fun View.getString(@StringRes res: Int): String {
    return context.getString(res)
}

fun View.getCompatColor(@ColorRes color: Int): Int {
    return context.getCompatColor(color)
}

fun View.getCompatDrawable(@DrawableRes res: Int): Drawable {
    return context.getCompatDrawable(res)
}

fun ImageView.tintImageRes(@ColorRes res: Int) {
    ImageViewCompat.setImageTintList(this, getCompatColorStateList(res))
}

fun ImageView.tintImage(@ColorInt color: Int) {
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color))
}

fun View.getCompatColorStateList(@ColorRes res: Int): ColorStateList {
    return context.getCompatColorState(res)
}

fun View.getSelectableItemBackGround(): Int {
    return context.getSelectableItemBackGround()
}

fun FrameLayout.setForegroundSelectableItemBackGround() {
    foreground = context.getCompatDrawable(getSelectableItemBackGround())
}

fun FrameLayout.setForegroundSelectableItemBackGroundBorderLess() {
    foreground = context.getCompatDrawable(getSelectableItemBorderless())
}

fun View.getSelectableItemBorderless(): Int {
    return context.getSelectableItemBorderless()
}

fun View.getStatusBarHeight(): Int {
    return context.getStatusBarHeight()
}

fun View.getToolbarHeight(): Int {
    return context.getToolbarHeight()
}

fun RecyclerView.checkFabShow(fab: FloatingActionButton, fabEnabledProvider: FabEnabledProvider) {
    if (fabEnabledProvider.isFabEnabled()) {
        val layoutManager = this.layoutManager
        if (layoutManager is LinearLayoutManager) {
            if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0
                    && layoutManager.findLastCompletelyVisibleItemPosition() == this.adapter!!.itemCount - 1) {
                fab.show()
            }
        }
    }
}

fun View.isOrientationLandscape(): Boolean {
    return context.isOrientationLandscape()
}