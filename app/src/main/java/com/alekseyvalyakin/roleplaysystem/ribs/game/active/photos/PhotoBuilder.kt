package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo.PhotoInGameRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameDependencyProvider
import com.uber.rib.core.BaseViewBuilder
import com.uber.rib.core.InteractorBaseComponent
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link PhotoScope}.
 */
class PhotoBuilder(dependency: ParentComponent) : BaseViewBuilder<PhotoView, PhotoRouter, PhotoBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [PhotoRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [PhotoRouter].
     */
    override fun build(parentViewGroup: ViewGroup): PhotoRouter {
        val view = createView(parentViewGroup)
        val interactor = PhotoInteractor()
        val component = DaggerPhotoBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.photoRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): PhotoView? {
        return PhotoView(parentViewGroup.context)
    }

    interface ParentComponent : ActiveGameDependencyProvider
    @dagger.Module
    abstract class Module {

        @PhotoScope
        @Binds
        internal abstract fun presenter(view: PhotoView): PhotoPresenter

        @dagger.Module
        companion object {

            @PhotoScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: PhotoView,
                    interactor: PhotoInteractor): PhotoRouter {
                return PhotoRouter(view, interactor, component)
            }

            @PhotoScope
            @Provides
            @JvmStatic
            internal fun photoViewModelProvider(
                    photoInGameRepository: PhotoInGameRepository,
                    game: Game,
                    userRepository: UserRepository): PhotoViewModelProvider {
                return PhotoViewModelProviderImpl(photoInGameRepository, game, userRepository)
            }

        }

    }

    @PhotoScope
    @dagger.Component(modules = arrayOf(Module::class), dependencies = arrayOf(ParentComponent::class))
    interface Component : InteractorBaseComponent<PhotoInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: PhotoInteractor): Builder

            @BindsInstance
            fun view(view: PhotoView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun photoRouter(): PhotoRouter
    }

    @Scope
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class PhotoScope

    @Qualifier
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class PhotoInternal
}
