package com.alekseyvalyakin.roleplaysystem.utils

import android.support.design.chip.Chip
import android.support.design.chip.ChipGroup
import android.support.design.widget.FloatingActionButton
import android.view.ViewManager
import com.alekseyvalyakin.roleplaysystem.views.ButtonsView
import com.alekseyvalyakin.roleplaysystem.views.SearchToolbar
import com.alekseyvalyakin.roleplaysystem.views.fabmenu.FabMenu
import com.alekseyvalyakin.roleplaysystem.views.player.PlayerView
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.flexbox.FlexboxLayout
import com.google.android.gms.common.SignInButton
import org.jetbrains.anko.custom.ankoView

@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.googleSignInButton(theme: Int = 0) = googleSignInButton({}, theme)

inline fun ViewManager.googleSignInButton(init: SignInButton.() -> Unit) = googleSignInButton(init, 0)

inline fun ViewManager.googleSignInButton(init: SignInButton.() -> Unit, theme: Int = 0) = ankoView(::SignInButton, theme, init)

@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.searchToolbar(theme: Int = 0, mode: SearchToolbar.Mode = SearchToolbar.Mode.CLASSIC) = searchToolbar({}, theme, mode)

@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.searchToolbar(init: SearchToolbar.() -> Unit) = searchToolbar({}, SearchToolbar.Mode.CLASSIC)

inline fun ViewManager.searchToolbar(init: SearchToolbar.() -> Unit, mode: SearchToolbar.Mode = SearchToolbar.Mode.CLASSIC) = searchToolbar(init, 0, mode)

inline fun ViewManager.searchToolbar(init: SearchToolbar.() -> Unit, theme: Int = 0, mode: SearchToolbar.Mode = SearchToolbar.Mode.CLASSIC) = ankoView({
    SearchToolbar(it, mode)
}, theme, init)

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

@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.playerView(theme: Int = 0) = playerView({}, theme)

inline fun ViewManager.playerView(init: PlayerView.() -> Unit) = playerView(init, 0)

inline fun ViewManager.playerView(init: PlayerView.() -> Unit, theme: Int = 0) = ankoView(::PlayerView, theme, init)


@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.fabMenu(theme: Int = 0, floatingActionButton: FloatingActionButton) = fabMenu({}, theme, floatingActionButton)

inline fun ViewManager.fabMenu(init: FabMenu.() -> Unit, floatingActionButton: FloatingActionButton) = fabMenu(init, 0, floatingActionButton)

inline fun ViewManager.fabMenu(init: FabMenu.() -> Unit, theme: Int = 0, floatingActionButton: FloatingActionButton) = ankoView({ FabMenu(it, floatingActionButton) }, theme, init)

@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.buttonsView(theme: Int = 0, btnInfoList: List<ButtonsView.ButtonInfo>) = buttonsView({}, theme, btnInfoList)

inline fun ViewManager.buttonsView(init: ButtonsView.() -> Unit = {}, btnInfoList: List<ButtonsView.ButtonInfo>) = buttonsView(init, 0, btnInfoList)

inline fun ViewManager.buttonsView(init: ButtonsView.() -> Unit, theme: Int = 0, btnInfoList: List<ButtonsView.ButtonInfo>) = ankoView({ ButtonsView(it, btnInfoList) }, theme, init)@Suppress("NOTHING_TO_INLINE")

inline fun ViewManager.flexboxLayout(theme: Int = 0) = flexboxLayout({}, theme)

inline fun ViewManager.flexboxLayout(init: FlexboxLayout.() -> Unit = {}) = flexboxLayout(init, 0)

inline fun ViewManager.flexboxLayout(init: FlexboxLayout.() -> Unit, theme: Int = 0) = ankoView(::FlexboxLayout, theme, init)