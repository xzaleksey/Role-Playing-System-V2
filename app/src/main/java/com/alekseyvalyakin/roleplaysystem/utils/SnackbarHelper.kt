package com.alekseyvalyakin.roleplaysystem.utils

import android.graphics.Color
import android.support.design.widget.Snackbar
import android.view.View

fun View.showSnack(text: CharSequence, durationMillis: Int = Snackbar.LENGTH_LONG) {
    val snackBar = Snackbar.make(this, text, durationMillis)
            .setActionTextColor(Color.WHITE)

    snackBar.show()
}
