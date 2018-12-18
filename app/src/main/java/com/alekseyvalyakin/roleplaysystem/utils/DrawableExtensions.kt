package com.alekseyvalyakin.roleplaysystem.utils

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt

fun Drawable.tint(@ColorInt color: Int): Drawable {
    this.setTint(color)
    return this
}