package com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.di.rib.RibDependencyProvider
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameDependencyProvider
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameEvent
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameListener
import com.alekseyvalyakin.roleplaysystem.ribs.profile.ProfileBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.profile.ProfileListener
import com.alekseyvalyakin.roleplaysystem.viewmodel.profile.ProfileListViewModelProviderImpl
import com.jakewharton.rxrelay2.Relay
import com.uber.rib.core.BaseViewBuilder
import com.uber.rib.core.InteractorBaseComponent
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope

class MenuBuilder(dependency: ParentComponent) : BaseViewBuilder<MenuView, MenuBuilder.ParentComponent>(dependency) {

    override fun build(parentViewGroup: ViewGroup): MenuRouter {
        val view = createView(parentViewGroup)
        val interactor = MenuInteractor()
        val component = DaggerMenuBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.menuRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): MenuView? {
        return MenuView(parentViewGroup.context)
    }

    interface ParentComponent : ActiveGameDependencyProvider

    @dagger.Module
    abstract class Module {

        @MenuScope
        @Binds
        internal abstract fun presenter(view: MenuView): MenuPresenter

        @dagger.Module
        companion object {

            @MenuScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: MenuView,
                    interactor: MenuInteractor,
                    relay: Relay<ActiveGameEvent>): MenuRouter {
                return MenuRouter(view, interactor, component,
                        ProfileBuilder(component),
                        CreateGameBuilder(component),
                        ActiveGameBuilder(component),
                        relay)
            }


            @MenuScope
            @Provides
            @JvmStatic
            internal fun profileListener(menuRouter: MenuRouter): ProfileListener {
                return menuRouter
            }

            @MenuScope
            @Provides
            @JvmStatic
            internal fun createGameListener(menuRouter: MenuRouter): CreateGameListener {
                return menuRouter
            }

            @MenuScope
            @Provides
            @JvmStatic
            internal fun menuViewModelProvider(userRepository: UserRepository,
                                               resourcesProvider: ResourcesProvider,
                                               stringRepository: StringRepository): MenuViewModelProvider {
                return MenuViewModelProviderImpl(ProfileListViewModelProviderImpl(userRepository, resourcesProvider, stringRepository),
                        resourcesProvider)
            }
        }
    }

    @MenuScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<MenuInteractor>, BuilderComponent,
            RibDependencyProvider, ProfileBuilder.ParentComponent, CreateGameBuilder.ParentComponent, ActiveGameBuilder.ParentComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: MenuInteractor): Builder

            @BindsInstance
            fun view(view: MenuView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun menuRouter(): MenuRouter
    }

    @Scope
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class MenuScope

    @Qualifier
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class MenuInternal
}
