package com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.di.rib.RibDependencyProvider
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameDependencyProvider
import com.uber.rib.core.BaseViewBuilder
import com.uber.rib.core.InteractorBaseComponent
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope

class MenuBuilder(dependency: ParentComponent) : BaseViewBuilder<MenuView, MenuRouter, MenuBuilder.ParentComponent>(dependency) {

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
                    interactor: MenuInteractor): MenuRouter {
                return MenuRouter(view, interactor, component)
            }
        }
    }

    @MenuScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<MenuInteractor>, BuilderComponent,
            RibDependencyProvider {

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
