package com.alekseyvalyakin.roleplaysystem.utils

import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.widget.TextViewCompat
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R

fun TextView.setSanserifMediumTypeface() {
    typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
}

fun TextView.setRufinaRegularTypeface() {
    typeface = ResourcesCompat.getFont(context, R.font.rufina_regular)
}

fun TextView.setAutoSizeTypeUniform(){
    TextViewCompat.setAutoSizeTextTypeWithDefaults(this, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM)
}