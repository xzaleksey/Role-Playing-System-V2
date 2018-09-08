package com.alekseyvalyakin.roleplaysystem.views

import android.content.Context
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.utils.getIntDimen
import com.alekseyvalyakin.roleplaysystem.utils.setTextSizeFromRes
import org.jetbrains.anko.*

class DiceCollectionSingleItemView(context: Context) : _LinearLayout(context) {
    val image: ImageView
    val textView: TextView

    init {
        orientation = LinearLayout.VERTICAL
        image = imageView {

        }.lparams(width = getIntDimen(R.dimen.dp_18), height = getIntDimen(R.dimen.dp_18)) {
            gravity = Gravity.CENTER
            bottomMargin = getIntDimen(R.dimen.dp_4)
        }
        textView = textView {
            id = R.id.tv_main
            gravity = Gravity.CENTER
            textColorResource = R.color.colorPrimary
            setTextSizeFromRes(R.dimen.dp_12)
        }.lparams(width = matchParent)
    }
}