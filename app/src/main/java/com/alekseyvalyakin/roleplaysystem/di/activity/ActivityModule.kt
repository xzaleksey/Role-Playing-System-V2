package com.alekseyvalyakin.roleplaysystem.di.activity

import com.alekseyvalyakin.roleplaysystem.app.MainActivity
import com.alekseyvalyakin.roleplaysystem.data.auth.GoogleSignInProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
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

}
