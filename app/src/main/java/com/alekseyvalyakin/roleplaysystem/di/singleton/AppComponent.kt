package com.alekseyvalyakin.roleplaysystem.di.singleton

import com.alekseyvalyakin.roleplaysystem.data.sound.SoundPlayService
import com.alekseyvalyakin.roleplaysystem.data.sound.SoundRecordService
import com.alekseyvalyakin.roleplaysystem.data.workmanager.PhotoInGameUploadWorker
import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityComponent
import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityModule
import dagger.Component
import javax.inject.Singleton

/**
 * Base app component
 */
@Singleton
@Component(modules = [AppModule::class, ProvidersModule::class])
interface AppComponent : SingletonDependencyProvider {
    fun activityComponent(activityModule: ActivityModule): ActivityComponent

    fun inject(photoInGameUploadWorker: PhotoInGameUploadWorker)

    fun inject(photoInGameUploadWorker: SoundRecordService)

    fun inject(soundPlayService: SoundPlayService)
}
