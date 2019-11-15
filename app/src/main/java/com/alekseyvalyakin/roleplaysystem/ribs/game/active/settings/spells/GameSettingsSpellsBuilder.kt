package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.spells

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

class GameSettingsSpellsBuilder(dependency: ParentComponent) : BaseViewBuilder<GameSettingsSpellsView, GameSettingsSpellsBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [GameSettingsSpellsRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [GameSettingsSpellsRouter].
     */
    override fun build(parentViewGroup: ViewGroup): GameSettingsSpellsRouter {
        val view = createView(parentViewGroup)
        val interactor = GameSettingsSpellsInteractor()
        val component = DaggerGameSettingsSpellsBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.gamesettingsspellsRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): GameSettingsSpellsView? {
        return GameSettingsSpellsView(parentViewGroup.context)
    }

    interface ParentComponent : ActiveGameDependencyProvider

    @dagger.Module
    abstract class Module {

        @GameSettingsSpellsScope
        @Binds
        internal abstract fun presenter(view: GameSettingsSpellsView): GameSettingsSpellsPresenter

        @dagger.Module
        companion object {

            @GameSettingsSpellsScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: GameSettingsSpellsView,
                    interactor: GameSettingsSpellsInteractor): GameSettingsSpellsRouter {
                return GameSettingsSpellsRouter(view, interactor, component)
            }
        }

    }

    @GameSettingsSpellsScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<GameSettingsSpellsInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: GameSettingsSpellsInteractor): Builder

            @BindsInstance
            fun view(view: GameSettingsSpellsView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun gamesettingsspellsRouter(): GameSettingsSpellsRouter
    }

    @Scope
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class GameSettingsSpellsScope

    @Qualifier
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class GameSettingsSpellsInternal
}
