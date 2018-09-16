package com.alekseyvalyakin.roleplaysystem.di.singleton

import android.arch.persistence.room.Room
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.alekseyvalyakin.roleplaysystem.BuildConfig
import com.alekseyvalyakin.roleplaysystem.app.RpsApp
import com.alekseyvalyakin.roleplaysystem.crypto.SimpleCryptoProvider
import com.alekseyvalyakin.roleplaysystem.crypto.SimpleCryptoProviderImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.GameRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.GameRepositoryImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.dice.DicesRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.dice.DicesRepositoryImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.gamesinuser.GamesInUserRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.gamesinuser.GamesInUserRepositoryImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo.PhotoInGameRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo.PhotoInGameRepositoryIml
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.useringame.UserInGameRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.useringame.UserInGameRepositoryImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepositoryImpl
import com.alekseyvalyakin.roleplaysystem.data.prefs.LocalKeyValueStorage
import com.alekseyvalyakin.roleplaysystem.data.prefs.SharedPreferencesHelper
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProviderImpl
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepositoryImpl
import com.alekseyvalyakin.roleplaysystem.data.room.AppDatabase
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGameDao
import com.alekseyvalyakin.roleplaysystem.data.useravatar.UserAvatarRepository
import com.alekseyvalyakin.roleplaysystem.data.useravatar.UserAvatarRepositoryImpl
import com.alekseyvalyakin.roleplaysystem.data.workmanager.WorkManagerWrapper
import com.alekseyvalyakin.roleplaysystem.data.workmanager.WorkManagerWrapperImpl
import com.alekseyvalyakin.roleplaysystem.di.activity.ThreadConfig
import com.alekseyvalyakin.roleplaysystem.ribs.main.CreateEmptyGameObservableProvider
import com.alekseyvalyakin.roleplaysystem.ribs.main.CreateEmptyGameObservableProviderImpl
import com.alekseyvalyakin.roleplaysystem.utils.NotificationInteractor
import com.alekseyvalyakin.roleplaysystem.utils.NotificationInteractorImpl
import com.alekseyvalyakin.roleplaysystem.utils.file.FileInfoProvider
import com.alekseyvalyakin.roleplaysystem.utils.file.FileInfoProviderImpl
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporterImpl
import com.uber.rib.core.RouterNavigatorFactory
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

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesHelper(sharedPreferences: SharedPreferences): LocalKeyValueStorage {
        return SharedPreferencesHelper(sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideResourceProvider(context: Context): ResourcesProvider {
        return ResourcesProviderImpl(context)
    }

    @Provides
    @Singleton
    fun provideStringRepo(resourcesProvider: ResourcesProvider): StringRepository {
        return StringRepositoryImpl(resourcesProvider)
    }

    @Provides
    @Singleton
    fun provideRouterNavigatorFactory(): RouterNavigatorFactory {
        return RouterNavigatorFactory(null)
    }

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository {
        return UserRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideGameRepository(userRepository: UserRepository,
                              userInGameRepository: UserInGameRepository, gamesInUserRepository: GamesInUserRepository): GameRepository {
        return GameRepositoryImpl(userRepository, userInGameRepository, gamesInUserRepository)
    }

    @Provides
    @Singleton
    fun provideUserInGameRepository(userRepository: UserRepository): UserInGameRepository {
        return UserInGameRepositoryImpl(userRepository)
    }

    @Provides
    @Singleton
    fun provideGamesInUserRepository(userRepository: UserRepository): GamesInUserRepository {
        return GamesInUserRepositoryImpl(userRepository)
    }

    @Provides
    @Singleton
    fun provideUserAvatarRepository(userAvatarRepositoryImpl: UserAvatarRepositoryImpl): UserAvatarRepository {
        return userAvatarRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideDiceRepository(userRepository: UserRepository): DicesRepository {
        return DicesRepositoryImpl(userRepository)
    }

    @Provides
    @Singleton
    fun provideSimpleCryptoProvider(): SimpleCryptoProvider {
        return SimpleCryptoProviderImpl(BuildConfig.MASTER_PASSWORD)
    }

    @Provides
    @Singleton
    fun provideFileProvider(context: Context): FileInfoProvider {
        return FileInfoProviderImpl(context)
    }

    @Provides
    @Singleton
    fun provideAnalyticsReporter(context: Context): AnalyticsReporter {
        return AnalyticsReporterImpl(context)
    }

    @Provides
    @Singleton
    fun createGameObservableProvider(gameRepository: GameRepository): CreateEmptyGameObservableProvider {
        return CreateEmptyGameObservableProviderImpl(gameRepository)
    }

    @Provides
    @Singleton
    fun dataBase(context: Context): AppDatabase {
        return Room.databaseBuilder(context,
                AppDatabase::class.java,
                "rps_database")
                .build()
    }

    @Provides
    @Singleton
    fun photoInGameDao(appDatabase: AppDatabase): PhotoInGameDao {
        return appDatabase.photoInGameDao()
    }

    @Provides
    @Singleton
    fun photoInGameRepository(): PhotoInGameRepository {
        return PhotoInGameRepositoryIml()
    }

    @Provides
    @Singleton
    fun workManagerWrapper(photoInGameDao: PhotoInGameDao): WorkManagerWrapper {
        return WorkManagerWrapperImpl(photoInGameDao)
    }

    @Provides
    @Singleton
    fun notificationInteractor(context: Context): NotificationInteractor {
        return NotificationInteractorImpl(context)
    }


}
