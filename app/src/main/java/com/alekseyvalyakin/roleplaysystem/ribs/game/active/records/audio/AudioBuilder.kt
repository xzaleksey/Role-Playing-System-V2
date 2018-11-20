package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.base.filter.FilterModel
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.data.sound.AudioFileInteractor
import com.alekseyvalyakin.roleplaysystem.di.rib.RibDependencyProvider
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.RecordsBuilder
import com.alekseyvalyakin.roleplaysystem.utils.file.FileInfoProvider
import com.uber.rib.core.BaseViewBuilder
import com.uber.rib.core.InteractorBaseComponent
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import io.reactivex.Flowable
import javax.inject.Qualifier
import javax.inject.Scope

class AudioBuilder(dependency: ParentComponent) : BaseViewBuilder<AudioView, AudioRouter, AudioBuilder.ParentComponent>(dependency) {

    override fun build(parentViewGroup: ViewGroup): AudioRouter {
        val view = createView(parentViewGroup)
        val interactor = AudioInteractor()
        val component = DaggerAudioBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.audioRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): AudioView? {
        return AudioView(parentViewGroup.context)
    }

    interface ParentComponent : RecordsBuilder.RecordComponent

    @dagger.Module
    abstract class Module {

        @AudioScope
        @Binds
        internal abstract fun presenter(view: AudioView): AudioPresenter

        @dagger.Module
        companion object {

            @AudioScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: AudioView,
                    interactor: AudioInteractor): AudioRouter {
                return AudioRouter(view, interactor, component)
            }

            @AudioScope
            @Provides
            @JvmStatic
            internal fun viewModelProvider(stringRepository: StringRepository,
                                           filterModelFlowable: Flowable<FilterModel>,
                                           fileInfoProvider: FileInfoProvider,
                                           audioFileInteractor: AudioFileInteractor,
                                           game: Game): AudioViewModelProvider {
                return AudioViewModelProviderImpl(
                        stringRepository,
                        filterModelFlowable,
                        fileInfoProvider,
                        audioFileInteractor,
                        game)
            }
        }
    }

    @AudioScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<AudioInteractor>, BuilderComponent,
            RibDependencyProvider {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: AudioInteractor): Builder

            @BindsInstance
            fun view(view: AudioView): Builder

            fun parentComponent(component: ParentComponent): Builder

            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun audioRouter(): AudioRouter
    }

    @Scope
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class AudioScope

    @Qualifier
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class AudioInternal
}
