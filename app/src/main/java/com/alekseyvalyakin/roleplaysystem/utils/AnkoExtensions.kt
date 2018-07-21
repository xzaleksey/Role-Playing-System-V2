package com.alekseyvalyakin.roleplaysystem.utils

import android.view.ViewManager
import com.google.android.gms.common.SignInButton
import org.jetbrains.anko.custom.ankoView

@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.googleSignInButton(theme: Int = 0) = googleSignInButton({}, theme)

inline fun ViewManager.googleSignInButton(init: SignInButton.() -> Unit, theme: Int = 0) = ankoView(::SignInButton, theme, init)

