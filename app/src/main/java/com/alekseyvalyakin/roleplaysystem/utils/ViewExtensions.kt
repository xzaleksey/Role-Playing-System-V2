package com.alekseyvalyakin.roleplaysystem.utils

import android.support.annotation.DimenRes
import android.util.TypedValue
import android.view.View
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