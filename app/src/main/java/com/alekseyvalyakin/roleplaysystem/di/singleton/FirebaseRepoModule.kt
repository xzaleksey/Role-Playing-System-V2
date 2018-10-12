package com.alekseyvalyakin.roleplaysystem.di.singleton

import com.alekseyvalyakin.roleplaysystem.data.firestorage.FirebaseStorageRepository
import com.alekseyvalyakin.roleplaysystem.data.firestorage.FirebaseStorageRepositoryImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.GameRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.GameRepositoryImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.dice.DicesRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.dice.DicesRepositoryImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.gamesinuser.GamesInUserRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.gamesinuser.GamesInUserRepositoryImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo.PhotoInGameRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo.PhotoInGameRepositoryIml
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes.DefaultSettingClassRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes.DefaultSettingClassRepositoryImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes.GameClassRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.classes.GameClassRepositoryImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.races.DefaultSettingRaceRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.races.DefaultSettingRaceRepositoryImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.races.GameRaceRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.races.GameRaceRepositoryImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills.GameSkillsRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills.GameSkillsRepositoryImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats.DefaultSettingStatsRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats.DefaultSettingStatsRepositoryImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats.GameStatsRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats.GameStatsRepositoryImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.useringame.UserInGameRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.useringame.UserInGameRepositoryImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.tags.GameTagsRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.tags.GameTagsRepositoryImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseRepoModule {

    @Provides
    @Singleton
    fun firebaseStorageRepo(): FirebaseStorageRepository {
        return FirebaseStorageRepositoryImpl()
    }

    @Provides
    @Singleton
    fun defaultStatRepository(): DefaultSettingStatsRepository {
        return DefaultSettingStatsRepositoryImpl()
    }

    @Provides
    @Singleton
    fun defaultClassesRepo(): DefaultSettingClassRepository {
        return DefaultSettingClassRepositoryImpl()
    }

    @Provides
    @Singleton
    fun photoInGameRepository(): PhotoInGameRepository {
        return PhotoInGameRepositoryIml()
    }

    @Provides
    @Singleton
    fun provideDiceRepository(userRepository: UserRepository): DicesRepository {
        return DicesRepositoryImpl(userRepository)
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
    fun provideUserRepository(): UserRepository {
        return UserRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideGamesInUserRepository(userRepository: UserRepository): GamesInUserRepository {
        return GamesInUserRepositoryImpl(userRepository)
    }

    @Provides
    @Singleton
    fun statsRepo(): GameStatsRepository {
        return GameStatsRepositoryImpl()
    }

    @Provides
    @Singleton
    fun classesRepo(): GameClassRepository {
        return GameClassRepositoryImpl()
    }

    @Provides
    @Singleton
    fun tagsRepo(): GameTagsRepository {
        return GameTagsRepositoryImpl()
    }

    @Provides
    @Singleton
    fun gameSkillsRepo(): GameSkillsRepository {
        return GameSkillsRepositoryImpl()
    }

    @Provides
    @Singleton
    fun gameRacesRepo(): GameRaceRepository {
        return GameRaceRepositoryImpl()
    }

    @Provides
    @Singleton
    fun defaultGameRacesRepo(): DefaultSettingRaceRepository {
        return DefaultSettingRaceRepositoryImpl()
    }

}