package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.SettingsDependencyProvider
import com.uber.rib.core.BaseViewBuilder
import com.uber.rib.core.InteractorBaseComponent
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link GameSettingsSkillsScope}.
 *
 */
class GameSettingsSkillsBuilder(dependency: ParentComponent) : BaseViewBuilder<GameSettingsSkillsView, GameSettingsSkillsRouter, GameSettingsSkillsBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [GameSettingsSkillsRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [GameSettingsSkillsRouter].
     */
    override fun build(parentViewGroup: ViewGroup): GameSettingsSkillsRouter {
        val view = createView(parentViewGroup)
        val interactor = GameSettingsSkillsInteractor()
        val component = DaggerGameSettingsSkillsBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.gamesettingsskillsRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): GameSettingsSkillsView? {
        return GameSettingsSkillsView(parentViewGroup.context)
    }

    interface ParentComponent : SettingsDependencyProvider

    @dagger.Module
    abstract class Module {

        @GameSettingsSkillsScope
        @Binds
        internal abstract fun presenter(view: GameSettingsSkillsView): GameSettingsSkillsPresenter

        @dagger.Module
        companion object {

            @GameSettingsSkillsScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: GameSettingsSkillsView,
                    interactor: GameSettingsSkillsInteractor): GameSettingsSkillsRouter {
                return GameSettingsSkillsRouter(view, interactor, component)
            }
        }

    }

    @GameSettingsSkillsScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<GameSettingsSkillsInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: GameSettingsSkillsInteractor): Builder

            @BindsInstance
            fun view(view: GameSettingsSkillsView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun gamesettingsskillsRouter(): GameSettingsSkillsRouter
    }

    @Scope
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class GameSettingsSkillsScope

    @Qualifier
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class GameSettingsSkillsInternal
}
