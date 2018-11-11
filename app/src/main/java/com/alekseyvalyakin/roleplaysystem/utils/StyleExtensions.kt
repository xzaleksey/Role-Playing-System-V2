package com.alekseyvalyakin.roleplaysystem.utils

import android.view.View
import android.widget.TextView
import com.alekseyvalyakin.roleplaysystem.R
import org.jetbrains.anko.textSizeDimen

fun View.setBackgroundElevation1() {
    elevation = getFloatDimen(R.dimen.dp_1)
}

fun View.setBackgroundElevation2() {
    elevation = getFloatDimen(R.dimen.dp_2)
}

fun View.setBackgroundElevation4() {
    elevation = getFloatDimen(R.dimen.dp_4)
}

fun View.setBackgroundElevation8() {
    elevation = getFloatDimen(R.dimen.dp_8)
}

fun View.setBackgroundElevation16() {
    elevation = getFloatDimen(R.dimen.dp_16)
}

fun TextView.h3Style() {
    setSanserifRegularTypeface()
    textSizeDimen = R.dimen.dp_48
}

fun TextView.h4Style() {
    setSanserifRegularTypeface()
    textSizeDimen = R.dimen.dp_34
}

fun TextView.h5Style() {
    setSanserifRegularTypeface()
    textSizeDimen = R.dimen.dp_24
}

fun TextView.h6Style() {
    setSanserifMediumTypeface()
    textSizeDimen = R.dimen.dp_20
}

fun TextView.subtitle1Style() {
    setSanserifRegularTypeface()
    textSizeDimen = R.dimen.dp_18
}

fun TextView.subtitle2Style() {
    setSanserifMediumTypeface()
    textSizeDimen = R.dimen.dp_14
}

fun TextView.body1Style() {
    setSanserifRegularTypeface()
    textSizeDimen = R.dimen.dp_16
}

fun TextView.body2Style() {
    setSanserifRegularTypeface()
    textSizeDimen = R.dimen.dp_14
}

fun TextView.buttonStyle() {
    setSanserifMediumTypeface()
    isAllCaps = true
    textSizeDimen = R.dimen.dp_14
}

fun TextView.captionStyle() {
    setSanserifRegularTypeface()
    textSizeDimen = R.dimen.dp_14
}

fun TextView.overlineStyle() {
    setSanserifRegularTypeface()
    isAllCaps = true
    textSizeDimen = R.dimen.dp_10
}