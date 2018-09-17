package com.alekseyvalyakin.roleplaysystem.ribs.game.active.characters

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
class GameCharactersBuilder(dependency: ParentComponent) : BaseViewBuilder<GameCharactersView, GameCharactersRouter, GameCharactersBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [GameCharactersRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [GameCharactersRouter].
     */
    override fun build(parentViewGroup: ViewGroup): GameCharactersRouter {
        val view = createView(parentViewGroup)
        val interactor = GameCharactersInteractor()
        val component = DaggerGameCharactersBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.gamesettingsRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): GameCharactersView? {
        return GameCharactersView(parentViewGroup.context)
    }

    interface ParentComponent : ActiveGameDependencyProvider

    @dagger.Module
    abstract class Module {

        @GameCharactersScope
        @Binds
        internal abstract fun presenter(view: GameCharactersView): GameCharactersInteractor.GameCharactersPresenter

        @dagger.Module
        companion object {

            @GameCharactersScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: GameCharactersView,
                    interactor: GameCharactersInteractor): GameCharactersRouter {
                return GameCharactersRouter(view, interactor, component)
            }
        }

    }

    @GameCharactersScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<GameCharactersInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: GameCharactersInteractor): Builder

            @BindsInstance
            fun view(view: GameCharactersView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun gamesettingsRouter(): GameCharactersRouter
    }

    @Scope
    @Retention(AnnotationRetention.BINARY)
    internal annotation class GameCharactersScope

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    internal annotation class GameCharactersInternal
}
