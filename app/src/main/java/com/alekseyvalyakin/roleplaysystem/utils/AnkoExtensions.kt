package com.alekseyvalyakin.roleplaysystem.utils

import android.support.design.chip.Chip
import android.support.design.chip.ChipGroup
import android.view.ViewManager
import com.alekseyvalyakin.roleplaysystem.views.SearchToolbar
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.gms.common.SignInButton
import org.jetbrains.anko.custom.ankoView

@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.googleSignInButton(theme: Int = 0) = googleSignInButton({}, theme)

inline fun ViewManager.googleSignInButton(init: SignInButton.() -> Unit) = googleSignInButton(init, 0)

inline fun ViewManager.googleSignInButton(init: SignInButton.() -> Unit, theme: Int = 0) = ankoView(::SignInButton, theme, init)

@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.searchToolbar(theme: Int = 0) = searchToolbar({}, theme)

inline fun ViewManager.searchToolbar(init: SearchToolbar.() -> Unit) = searchToolbar(init, 0)

inline fun ViewManager.searchToolbar(init: SearchToolbar.() -> Unit, theme: Int = 0) = ankoView(::SearchToolbar, theme, init)

@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.photoView(theme: Int = 0) = photoView({}, theme)

inline fun ViewManager.photoView(init: PhotoView.() -> Unit) = photoView(init, 0)

inline fun ViewManager.photoView(init: PhotoView.() -> Unit, theme: Int = 0) = ankoView(::PhotoView, theme, init)
@Suppress("NOTHING_TO_INLINE")

inline fun ViewManager.chipGroup(theme: Int = 0) = chipGroup({}, theme)

inline fun ViewManager.chipGroup(init: ChipGroup.() -> Unit) = chipGroup(init, 0)

inline fun ViewManager.chipGroup(init: ChipGroup.() -> Unit, theme: Int = 0) = ankoView(::ChipGroup, theme, init)

@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.chip(theme: Int = 0) = chip({}, theme)

inline fun ViewManager.chip(init: Chip.() -> Unit) = chip(init, 0)

inline fun ViewManager.chip(init: Chip.() -> Unit, theme: Int = 0) = ankoView(::Chip, theme, init)