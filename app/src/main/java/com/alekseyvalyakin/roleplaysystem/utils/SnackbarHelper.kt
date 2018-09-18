package com.alekseyvalyakin.roleplaysystem.utils

import android.graphics.Color
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import com.alekseyvalyakin.roleplaysystem.R

fun View.showSnack(text: CharSequence, durationMillis: Int = Snackbar.LENGTH_LONG) {
    val snackBar = Snackbar.make(this, text, durationMillis)
            .setActionTextColor(Color.WHITE)
    snackBar.view.setBackgroundColor(ContextCompat.getColor(snackBar.context, R.color.colorAccentLight))
    snackBar.show()
}
