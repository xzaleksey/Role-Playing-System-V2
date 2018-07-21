package com.alekseyvalyakin.roleplaysystem.di

import android.content.Context
import com.alekseyvalyakin.roleplaysystem.app.RpsApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Base app module
 */
@Module
class AppModule(private val mApp: RpsApp) {

    @Provides
    @Singleton
    internal fun provideApplicationContext(): Context {
        return mApp
    }
}
