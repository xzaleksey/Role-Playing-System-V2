package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.equip

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

class GameSettingsEquipBuilder(dependency: ParentComponent) : BaseViewBuilder<GameSettingsEquipView, GameSettingsEquipRouter, GameSettingsEquipBuilder.ParentComponent>(dependency) {

    override fun build(parentViewGroup: ViewGroup): GameSettingsEquipRouter {
        val view = createView(parentViewGroup)
        val interactor = GameSettingsEquipInteractor()
        val component = DaggerGameSettingsEquipBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.gamesettingsEquipRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): GameSettingsEquipView? {
        return GameSettingsEquipView(parentViewGroup.context)
    }

    interface ParentComponent : ActiveGameDependencyProvider

    @dagger.Module
    abstract class Module {

        @GameSettingsEquipScope
        @Binds
        internal abstract fun presenter(view: GameSettingsEquipView): GameSettingsEquipPresenter

        @dagger.Module
        companion object {

            @GameSettingsEquipScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: GameSettingsEquipView,
                    interactor: GameSettingsEquipInteractor): GameSettingsEquipRouter {
                return GameSettingsEquipRouter(view, interactor, component)
            }
        }

    }

    @GameSettingsEquipScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<GameSettingsEquipInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: GameSettingsEquipInteractor): Builder

            @BindsInstance
            fun view(view: GameSettingsEquipView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun gamesettingsEquipRouter(): GameSettingsEquipRouter
    }

    @Scope
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class GameSettingsEquipScope

    @Qualifier
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class GameSettingsEquipInternal
}
