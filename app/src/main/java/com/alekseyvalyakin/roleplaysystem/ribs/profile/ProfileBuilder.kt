package com.alekseyvalyakin.roleplaysystem.ribs.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.di.rib.RibDependencyProvider
import com.uber.rib.core.BaseViewBuilder
import com.uber.rib.core.InteractorBaseComponent
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link ProfileScope}.
 *
 */
class ProfileBuilder(dependency: ParentComponent) : BaseViewBuilder<ProfileView, ProfileRouter, ProfileBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [ProfileRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [ProfileRouter].
     */
    override fun build(parentViewGroup: ViewGroup): ProfileRouter {
        val view = createView(parentViewGroup)
        val interactor = ProfileInteractor()
        val component = DaggerProfileBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.profileRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): ProfileView {
        return ProfileView(parentViewGroup.context)
    }

    interface ParentComponent : RibDependencyProvider

    @dagger.Module
    abstract class Module {

        @ProfileScope
        @Binds
        internal abstract fun presenter(view: ProfileView): ProfileInteractor.ProfilePresenter

        @dagger.Module
        companion object {

            @ProfileScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: ProfileView,
                    interactor: ProfileInteractor): ProfileRouter {
                return ProfileRouter(view, interactor, component)
            }
        }

    }

    @ProfileScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<ProfileInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: ProfileInteractor): Builder

            @BindsInstance
            fun view(view: ProfileView): Builder

            fun parentComponent(component: ParentComponent): Builder

            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun profileRouter(): ProfileRouter
    }

    @Scope
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class ProfileScope

    @Qualifier
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class ProfileInternal
}
