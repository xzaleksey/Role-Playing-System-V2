package com.alekseyvalyakin.roleplaysystem.di.singleton

import com.alekseyvalyakin.roleplaysystem.crypto.SimpleCryptoProvider
import com.alekseyvalyakin.roleplaysystem.data.auth.AuthProvider
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.data.game.GameRepository
import com.alekseyvalyakin.roleplaysystem.data.game.useringame.UserInGameRepository
import com.alekseyvalyakin.roleplaysystem.data.prefs.LocalKeyValueStorage
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.di.activity.ThreadConfig
import com.uber.rib.core.RouterNavigatorFactory
import io.reactivex.Scheduler

interface SingletonDependencyProvider {
    @ThreadConfig(ThreadConfig.TYPE.UI)
    fun provideUiScheduler(): Scheduler

    @ThreadConfig(ThreadConfig.TYPE.IO)
    fun provideIoScheduler(): Scheduler

    @ThreadConfig(ThreadConfig.TYPE.COMPUTATATION)
    fun provideCompScheduler(): Scheduler

    fun provideAuthProvider(): AuthProvider

    fun provideLocalKeyValueStorage(): LocalKeyValueStorage

    fun provideStringRepository(): StringRepository

    fun userRepository(): UserRepository

    fun gameRepository(): GameRepository

    fun userInGameRepository(): UserInGameRepository

    fun routerNavigatorFactory(): RouterNavigatorFactory

    fun resourceProvider(): ResourcesProvider

    fun sinmpleCryptoRovider(): SimpleCryptoProvider
}