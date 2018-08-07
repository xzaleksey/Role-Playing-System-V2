package com.alekseyvalyakin.roleplaysystem.ribs.game.create

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.data.game.Game
import com.alekseyvalyakin.roleplaysystem.data.game.GameRepository
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.di.rib.RibDependencyProvider
import com.alekseyvalyakin.roleplaysystem.ribs.game.model.GameProvider
import com.alekseyvalyakin.roleplaysystem.ribs.game.model.GameProviderImpl
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link CreateGameScope}.
 *
 */
class CreateGameBuilder(dependency: ParentComponent) : ViewBuilder<CreateGameView, CreateGameRouter, CreateGameBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [CreateGameRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [CreateGameRouter].
     */
    fun build(parentViewGroup: ViewGroup, game: Game): CreateGameRouter {
        val view = createView(parentViewGroup)
        val interactor = CreateGameInteractor()
        val component = DaggerCreateGameBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .game(game)
                .build()
        return component.creategameRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): CreateGameView? {
        return CreateGameView(parentViewGroup.context)
    }

    interface ParentComponent : RibDependencyProvider {

    }

    @dagger.Module
    abstract class Module {

        @CreateGameScope
        @Binds
        internal abstract fun presenter(view: CreateGameView): CreateGameInteractor.CreateGamePresenter

        @dagger.Module
        companion object {

            @CreateGameScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: CreateGameView,
                    interactor: CreateGameInteractor,
                    gameProvider: GameProvider): CreateGameRouter {
                return CreateGameRouter(view, interactor, component, gameProvider)
            }

            @CreateGameScope
            @Provides
            @JvmStatic
            internal fun createGameProvider(
                    game: Game,
                    gameRepository: GameRepository
            ): GameProvider {
                return GameProviderImpl(gameRepository, game)
            }

            @CreateGameScope
            @Provides
            @JvmStatic
            internal fun createGameViewModelProvider(
                    stringRepository: StringRepository
            ): CreateGameViewModelProvider {
                return CreateGameViewModelProvider(stringRepository)
            }
        }
    }

    @CreateGameScope
    @dagger.Component(modules = arrayOf(Module::class), dependencies = [(ParentComponent::class)])
    interface Component : InteractorBaseComponent<CreateGameInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: CreateGameInteractor): Builder

            @BindsInstance
            fun view(view: CreateGameView): Builder

            fun parentComponent(component: ParentComponent): Builder

            @BindsInstance
            fun game(game: Game): Builder

            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun creategameRouter(): CreateGameRouter
    }

    @Scope
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    annotation class CreateGameScope

    @Qualifier
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    annotation class CreateGameInternal
}
