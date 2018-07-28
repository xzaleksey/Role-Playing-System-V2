package com.alekseyvalyakin.roleplaysystem.utils

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.widget.ImageViewCompat
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView

fun TextView.setTextSizeFromRes(@DimenRes res: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getFloatDimen(res))
}

fun View.getFloatDimen(@DimenRes res: Int): Float {
    return context.getFloatDimen(res)
}

fun View.getIntDimen(@DimenRes res: Int): Int {
    return context.getIntDimen(res)
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

fun ImageView.tintImage(@ColorRes res: Int) {
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(getCompatColor(res)))
}

fun View.getSelectableItemColor(): Int {
    return context.getSelectableItemColor()
}

fun View.getSelectableItemBorderless(): Int {
    return context.getSelectableItemBorderless()
}