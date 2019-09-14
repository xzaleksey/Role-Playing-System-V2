package com.alekseyvalyakin.roleplaysystem.di.activity

import com.alekseyvalyakin.roleplaysystem.app.MainActivity
import com.alekseyvalyakin.roleplaysystem.data.auth.GoogleSignInProvider
import com.alekseyvalyakin.roleplaysystem.ribs.dialogs.DialogDelegate
import com.alekseyvalyakin.roleplaysystem.utils.image.LocalImageProvider
import com.alekseyvalyakin.roleplaysystem.utils.keyboard.KeyboardStateProvider

interface ActivityDependencyProvider {
    fun provideGoogleSignInProvider(): GoogleSignInProvider

    fun provideActivityListener(): ActivityListener

    fun provideLocalImageProvider(): LocalImageProvider

    fun provideActivity(): MainActivity

    fun provideKeyboardStateProvider(): KeyboardStateProvider

    fun dialogDelegate(): DialogDelegate
}