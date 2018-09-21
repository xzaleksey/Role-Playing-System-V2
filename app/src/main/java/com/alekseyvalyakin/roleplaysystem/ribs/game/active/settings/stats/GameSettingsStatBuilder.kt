package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats.DefaultSettingStatsRepository
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityListener
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameDependencyProvider
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameEvent
import com.jakewharton.rxrelay2.Relay
import com.uber.rib.core.BaseViewBuilder
import com.uber.rib.core.InteractorBaseComponent
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.CLASS
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link GameSettingsStatScope}.
 *
 */
class GameSettingsStatBuilder(dependency: ParentComponent) : BaseViewBuilder<GameSettingsStatView, GameSettingsStatRouter, GameSettingsStatBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [GameSettingsStatRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [GameSettingsStatRouter].
     */
    override fun build(parentViewGroup: ViewGroup): GameSettingsStatRouter {
        val view = createView(parentViewGroup)
        val interactor = GameSettingsStatInteractor()
        val component = DaggerGameSettingsStatBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.gamesettingsstatRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): GameSettingsStatView? {
        return GameSettingsStatView(parentViewGroup.context)
    }

    interface ParentComponent : ActiveGameDependencyProvider

    @dagger.Module
    abstract class Module {

        @GameSettingsStatScope
        @Binds
        internal abstract fun presenter(view: GameSettingsStatView): GameSettingsStatPresenter

        @dagger.Module
        companion object {

            @GameSettingsStatScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: GameSettingsStatView,
                    interactor: GameSettingsStatInteractor): GameSettingsStatRouter {
                return GameSettingsStatRouter(view, interactor, component)
            }

            @GameSettingsStatScope
            @Provides
            @JvmStatic
            internal fun viewModelProvider(
                    view: GameSettingsStatView,
                    defaultSettingsStatsRepository: DefaultSettingStatsRepository,
                    game: Game,
                    stringRepository: StringRepository,
                    resourcesProvider: ResourcesProvider,
                    activityListener: ActivityListener,
                    activeGameEventRelay: Relay<ActiveGameEvent>
            ): GameSettingsStatsViewModelProvider {
                return GameSettingsStatsViewModelProviderImpl(
                        defaultSettingsStatsRepository,
                        game,
                        stringRepository,
                        resourcesProvider,
                        view,
                        activityListener,
                        activeGameEventRelay)
            }
        }

    }

    @GameSettingsStatScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<GameSettingsStatInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: GameSettingsStatInteractor): Builder

            @BindsInstance
            fun view(view: GameSettingsStatView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun gamesettingsstatRouter(): GameSettingsStatRouter
    }

    @Scope
    @Retention(CLASS)
    internal annotation class GameSettingsStatScope

    @Qualifier
    @Retention(CLASS)
    internal annotation class GameSettingsStatInternal
}
