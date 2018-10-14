package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.dices

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameDependencyProvider
import com.uber.rib.core.BaseViewBuilder
import com.uber.rib.core.InteractorBaseComponent
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope

class GameSettingsDicesBuilder(dependency: ParentComponent) : BaseViewBuilder<GameSettingsDicesView, GameSettingsDicesRouter, GameSettingsDicesBuilder.ParentComponent>(dependency) {

    override fun build(parentViewGroup: ViewGroup): GameSettingsDicesRouter {
        val view = createView(parentViewGroup)
        val interactor = GameSettingsDicesInteractor()
        val component = DaggerGameSettingsDicesBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.gamesettingsDicesRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): GameSettingsDicesView? {
        return GameSettingsDicesView(parentViewGroup.context)
    }

    interface ParentComponent : ActiveGameDependencyProvider

    @dagger.Module
    abstract class Module {

        @GameSettingsDicesScope
        @Binds
        internal abstract fun presenter(view: GameSettingsDicesView): GameSettingsDicesPresenter

        @dagger.Module
        companion object {

            @GameSettingsDicesScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: GameSettingsDicesView,
                    interactor: GameSettingsDicesInteractor): GameSettingsDicesRouter {
                return GameSettingsDicesRouter(view, interactor, component)
            }
        }

    }

    @GameSettingsDicesScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<GameSettingsDicesInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: GameSettingsDicesInteractor): Builder

            @BindsInstance
            fun view(view: GameSettingsDicesView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun gamesettingsDicesRouter(): GameSettingsDicesRouter
    }

    @Scope
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class GameSettingsDicesScope

    @Qualifier
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class GameSettingsDicesInternal
}
