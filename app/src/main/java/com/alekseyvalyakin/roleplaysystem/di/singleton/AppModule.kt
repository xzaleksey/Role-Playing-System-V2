package com.alekseyvalyakin.roleplaysystem.di.singleton

import android.content.Context
import com.alekseyvalyakin.roleplaysystem.app.RpsApp
import com.alekseyvalyakin.roleplaysystem.di.activity.ThreadConfig
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

/**
 * Base app module
 */
@Module
class AppModule(private val mApp: RpsApp) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return mApp
    }

    @Provides
    @Singleton
    @ThreadConfig(ThreadConfig.TYPE.UI)
    fun provideUiScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    @Provides
    @Singleton
    @ThreadConfig(ThreadConfig.TYPE.IO)
    fun provideIoScheduler(): Scheduler {
        return Schedulers.io()
    }

    @Provides
    @Singleton
    @ThreadConfig(ThreadConfig.TYPE.COMPUTATATION)
    fun provideCompScheduler(): Scheduler {
        return Schedulers.computation()
    }

}
