package com.alekseyvalyakin.roleplaysystem.di.activity

import com.alekseyvalyakin.roleplaysystem.data.auth.GoogleSignInProvider
import com.alekseyvalyakin.roleplaysystem.utils.image.LocalImageProvider

interface ActivityDependencyProvider {
    fun provideGoogleSignInProvider(): GoogleSignInProvider

    fun provideActivityListener(): ActivityListener

    fun provideLocalImageProvider(): LocalImageProvider
}