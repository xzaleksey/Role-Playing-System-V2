package com.alekseyvalyakin.roleplaysystem.di.activity

import com.alekseyvalyakin.roleplaysystem.data.auth.GoogleSignInProvider

interface ActivityDependencyProvider {
    fun provideGoogleSignInProvider(): GoogleSignInProvider
}