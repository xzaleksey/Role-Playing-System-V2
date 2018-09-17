package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.fullsizephoto

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.di.rib.RibDependencyProvider
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link FullSizePhotoScope}.
 *
 */
class FullSizePhotoBuilder(dependency: ParentComponent) : ViewBuilder<FullSizePhotoView, FullSizePhotoRouter, FullSizePhotoBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [FullSizePhotoRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [FullSizePhotoRouter].
     */
    fun build(parentViewGroup: ViewGroup, fullSizePhotoModel: FullSizePhotoModel): FullSizePhotoRouter {
        val view = createView(parentViewGroup)
        val interactor = FullSizePhotoInteractor()
        val component = DaggerFullSizePhotoBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .fullSizePhotoModel(fullSizePhotoModel)
                .build()
        return component.fullsizephotoRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): FullSizePhotoView? {
        return FullSizePhotoView(parentViewGroup.context)
    }

    interface ParentComponent : RibDependencyProvider

    @dagger.Module
    abstract class Module {

        @FullSizePhotoScope
        @Binds
        internal abstract fun presenter(view: FullSizePhotoView): FullSizePhotoInteractor.FullSizePhotoPresenter

        @dagger.Module
        companion object {

            @FullSizePhotoScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: FullSizePhotoView,
                    interactor: FullSizePhotoInteractor): FullSizePhotoRouter {
                return FullSizePhotoRouter(view, interactor, component)
            }
        }

    }

    @FullSizePhotoScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<FullSizePhotoInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: FullSizePhotoInteractor): Builder

            @BindsInstance
            fun view(view: FullSizePhotoView): Builder

            @BindsInstance
            fun fullSizePhotoModel(fullSizePhotoModel: FullSizePhotoModel): Builder

            fun parentComponent(component: ParentComponent): Builder

            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun fullsizephotoRouter(): FullSizePhotoRouter
    }

    @Scope
    @Retention(AnnotationRetention.BINARY)
    internal annotation class FullSizePhotoScope

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    internal annotation class FullSizePhotoInternal
}
