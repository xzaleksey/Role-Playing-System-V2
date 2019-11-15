package com.alekseyvalyakin.roleplaysystem.utils

import android.app.Activity
import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard(delay: Long = 10L, view: View?) {
    val focusedView = view ?: currentFocus ?: return
    if (delay == 0L) {
        hideKeyboard(focusedView)
        return
    }
    focusedView.postDelayed({
        if (!focusedView.isAttachedToWindow) {
            return@postDelayed
        }
        hideKeyboard(focusedView)
    }, delay)
}

private fun Activity.hideKeyboard(view: View) {
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

@JvmOverloads
fun View.hideKeyboard(delay: Long = 10L) {
    context.hideKeyboard(delay, this)
}

@JvmOverloads
fun Context.hideKeyboard(delay: Long = 10L, view: View? = null) {
    when (this) {
        is Activity -> this.hideKeyboard(delay, view)
        is ContextThemeWrapper -> baseContext?.hideKeyboard(delay, view)
    }
}

fun Activity.forceHideKeyboard() {
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