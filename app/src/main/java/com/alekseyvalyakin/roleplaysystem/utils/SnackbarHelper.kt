package com.alekseyvalyakin.roleplaysystem.utils

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import com.alekseyvalyakin.roleplaysystem.R
import com.google.android.material.snackbar.Snackbar

fun View.showSnack(text: CharSequence, durationMillis: Int = Snackbar.LENGTH_LONG) {
    val snackBar = Snackbar.make(this, text, durationMillis)
            .setActionTextColor(Color.WHITE)
    snackBar.view.setBackgroundColor(ContextCompat.getColor(snackBar.context, R.color.colorAccentLight))
    snackBar.show()
}
