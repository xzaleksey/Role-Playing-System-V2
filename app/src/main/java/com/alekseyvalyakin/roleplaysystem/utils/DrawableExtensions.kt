package com.alekseyvalyakin.roleplaysystem.utils

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt

fun Drawable.tint(@ColorInt color: Int): Drawable {
    this.setTint(color)
    return this
}

fun Drawable.tintColorState(colorStateList: ColorStateList): Drawable {
    this.setTintList(colorStateList)
    return this
}