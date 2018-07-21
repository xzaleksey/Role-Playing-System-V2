package com.alekseyvalyakin.roleplaysystem.utils

import android.content.Context
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.v4.content.ContextCompat

fun Context.getCompatColor(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Context.getIntDimen(@DimenRes res: Int): Int {
    return this.resources.getDimensionPixelSize(res)
}

fun Context.getFloatDimen(@DimenRes res: Int): Float {
    return this.resources.getDimension(res)
}