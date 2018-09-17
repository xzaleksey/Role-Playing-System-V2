package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings

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

/**
 * Builder for the {@link GameSettingsScope}.
 *
 */
class GameSettingsBuilder(dependency: ParentComponent) : BaseViewBuilder<GameSettingsView, GameSettingsRouter, GameSettingsBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [GameSettingsRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [GameSettingsRouter].
     */
    override fun build(parentViewGroup: ViewGroup): GameSettingsRouter {
        val view = createView(parentViewGroup)
        val interactor = GameSettingsInteractor()
        val component = DaggerGameSettingsBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.gamesettingsRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): GameSettingsView? {
        return GameSettingsView(parentViewGroup.context)
    }

    interface ParentComponent : ActiveGameDependencyProvider

    @dagger.Module
    abstract class Module {

        @GameSettingsScope
        @Binds
        internal abstract fun presenter(view: GameSettingsView): GameSettingsInteractor.GameSettingsPresenter

        @dagger.Module
        companion object {

            @GameSettingsScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: GameSettingsView,
                    interactor: GameSettingsInteractor): GameSettingsRouter {
                return GameSettingsRouter(view, interactor, component)
            }
        }

    }

    @GameSettingsScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<GameSettingsInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: GameSettingsInteractor): Builder

            @BindsInstance
            fun view(view: GameSettingsView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun gamesettingsRouter(): GameSettingsRouter
    }

    @Scope
    @Retention(AnnotationRetention.BINARY)
    internal annotation class GameSettingsScope

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    internal annotation class GameSettingsInternal
}
