package com.alekseyvalyakin.roleplaysystem.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

@JvmOverloads
fun Activity.hideKeyboard(delay: Long = 10L) {
    val view = currentFocus
    view?.postDelayed({
        if (!view.isAttachedToWindow) {
            return@postDelayed
        }
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }, delay)
}

@JvmOverloads
fun View.hideKeyboard(delay: Long = 10L) {
    (context as? Activity)?.hideKeyboard(delay)
}

@JvmOverloads
fun Context.hideKeyboard(delay: Long = 10L) {
    (this as? Activity)?.hideKeyboard(delay)
}

fun Activity.forceHideKeyboard(activity: Activity) {
    hideKeyboard(0L)
}

@JvmOverloads
fun View.showSoftKeyboard(delay: Long = 10L) {
    val context = context
    if (requestFocus()) {
        postDelayed({
            if (!this.isAttachedToWindow) {
                return@postDelayed
            }
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        }, delay)
    }
}

fun Activity.showKeyboard() {
    val view = currentFocus
    if (view != null) {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }
}

fun Activity.hideKeyboardNotEditText() {
    val view = currentFocus
    if (view != null && view !is android.widget.EditText) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}