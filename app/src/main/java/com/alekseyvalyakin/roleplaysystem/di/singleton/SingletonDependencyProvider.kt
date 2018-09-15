package com.alekseyvalyakin.roleplaysystem.di.singleton

import com.alekseyvalyakin.roleplaysystem.crypto.SimpleCryptoProvider
import com.alekseyvalyakin.roleplaysystem.data.auth.AuthProvider
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.GameRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.dice.DicesRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.gamesinuser.GamesInUserRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo.PhotoInGameRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.useringame.UserInGameRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.data.prefs.LocalKeyValueStorage
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGameDao
import com.alekseyvalyakin.roleplaysystem.data.useravatar.UserAvatarRepository
import com.alekseyvalyakin.roleplaysystem.data.workmanager.WorkManagerWrapper
import com.alekseyvalyakin.roleplaysystem.di.activity.ThreadConfig
import com.alekseyvalyakin.roleplaysystem.ribs.main.CreateEmptyGameObservableProvider
import com.alekseyvalyakin.roleplaysystem.utils.NotificationInteractor
import com.alekseyvalyakin.roleplaysystem.utils.file.FileInfoProvider
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
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

    fun gamesInGameRepository(): GamesInUserRepository

    fun routerNavigatorFactory(): RouterNavigatorFactory

    fun resourceProvider(): ResourcesProvider

    fun sinmpleCryptoRovider(): SimpleCryptoProvider

    fun fileInfoProvider(): FileInfoProvider

    fun userAvatarRepo(): UserAvatarRepository

    fun diceRepo(): DicesRepository

    fun analyticsReporter(): AnalyticsReporter

    fun createGameObservableProvider(): CreateEmptyGameObservableProvider

    fun photoInGameDao(): PhotoInGameDao

    fun photoInGameRepository(): PhotoInGameRepository

    fun workManagerWrapper(): WorkManagerWrapper

    fun notificationInteractor(): NotificationInteractor
}