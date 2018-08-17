package com.alekseyvalyakin.roleplaysystem.di.activity

import com.alekseyvalyakin.roleplaysystem.app.MainActivity
import com.alekseyvalyakin.roleplaysystem.data.auth.GoogleSignInProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.utils.image.LocalImageProvider
import com.alekseyvalyakin.roleplaysystem.utils.image.LocalImageProviderImpl
import dagger.Module
import dagger.Provides

/**
 * Base app module
 */
@Module
class ActivityModule(private val activity: MainActivity) {

    @Provides
    @ActivityScope
    fun provideGoogleSignInProvider(stringRepository: StringRepository): GoogleSignInProvider {
        return GoogleSignInProvider(activity, stringRepository)
    }

    @Provides
    @ActivityScope
    fun provideActivityListener(): ActivityListener {
        return ActivityListenerImpl(activity)
    }

    @Provides
    @ActivityScope
    fun provideLocalImageProvider(): LocalImageProvider {
        return LocalImageProviderImpl(activity)
    }
}
