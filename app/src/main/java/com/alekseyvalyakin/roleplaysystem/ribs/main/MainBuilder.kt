package com.alekseyvalyakin.roleplaysystem.ribs.main

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.di.rib.RibDependencyProvider
import com.uber.rib.core.BaseViewBuilder
import com.uber.rib.core.InteractorBaseComponent
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link MainScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
class MainBuilder(dependency: ParentComponent) : BaseViewBuilder<MainView, MainRouter, MainBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [MainRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [MainRouter].
     */
    override fun build(parentViewGroup: ViewGroup): MainRouter {
        val view = createView(parentViewGroup)
        val interactor = MainInteractor()
        val component = DaggerMainBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.mainRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): MainView? {
        return MainView(parentViewGroup.context)
    }

    interface ParentComponent : RibDependencyProvider {
    }

    @dagger.Module
    abstract class Module {

        @MainScope
        @Binds
        internal abstract fun presenter(view: MainView): MainInteractor.MainPresenter

        @dagger.Module
        companion object {

            @MainScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: MainView,
                    interactor: MainInteractor): MainRouter {
                return MainRouter(view, interactor, component)
            }

            @MainScope
            @Provides
            @JvmStatic
            internal fun mainViewModelProvider(
                    userRepository: UserRepository,
                    resourceProvider: ResourcesProvider,
                    stringRepo: StringRepository): MainViewModelProvider {
                return MainViewModelProviderImpl(userRepository, resourceProvider, stringRepo)
            }
        }

    }

    @MainScope
    @dagger.Component(modules = [(Module::class)], dependencies = [(ParentComponent::class)])
    interface Component : InteractorBaseComponent<MainInteractor>, BuilderComponent,
            RibDependencyProvider {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: MainInteractor): Builder

            @BindsInstance
            fun view(view: MainView): Builder

            fun parentComponent(component: ParentComponent): Builder

            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun mainRouter(): MainRouter
    }

    @Scope
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class MainScope

    @Qualifier
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class MainInternal
}
