package com.alekseyvalyakin.roleplaysystem.utils

import android.graphics.Typeface
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.widget.TextViewCompat
import android.text.TextUtils
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

fun TextView.getVisibleText(): String {
    if (this.layout == null) {
        return ""
    }

    val maxLines = TextViewCompat.getMaxLines(this)
    val lastLine = if (maxLines < 1 || maxLines > this.lineCount) this.lineCount else maxLines

    if (TextUtils.TruncateAt.END == this.ellipsize) {
        val ellCount = this.layout.getEllipsisCount(lastLine - 1)

        return if (ellCount > 0 && this.length() > ellCount) {
            this.text.toString().substring(0, this.text.length - ellCount)
        } else this.text.toString()

    }

    val end = this.layout.getLineEnd(lastLine - 1)
    return this.text.toString().substring(0, end)
}