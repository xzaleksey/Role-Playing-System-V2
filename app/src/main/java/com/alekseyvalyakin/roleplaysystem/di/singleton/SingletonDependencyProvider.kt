package com.alekseyvalyakin.roleplaysystem.di.singleton

import com.alekseyvalyakin.roleplaysystem.data.auth.AuthProvider
import com.alekseyvalyakin.roleplaysystem.di.activity.ThreadConfig
import io.reactivex.Scheduler

interface SingletonDependencyProvider {
    @ThreadConfig(ThreadConfig.TYPE.UI)
    fun provideUiScheduler(): Scheduler

    @ThreadConfig(ThreadConfig.TYPE.IO)
    fun provideIoScheduler(): Scheduler

    @ThreadConfig(ThreadConfig.TYPE.COMPUTATATION)
    fun provideCompScheduler(): Scheduler

    fun provideAuthProvider(): AuthProvider
}