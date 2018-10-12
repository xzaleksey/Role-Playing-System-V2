package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.races

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.races.DefaultSettingRaceRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.races.GameRaceRepository
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityListener
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameDependencyProvider
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameEvent
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.jakewharton.rxrelay2.Relay
import com.uber.rib.core.BaseViewBuilder
import com.uber.rib.core.InteractorBaseComponent
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link GameSettingsClassScope}.
 *
 */
class GameSettingsRaceBuilder(dependency: ParentComponent) : BaseViewBuilder<GameSettingsRaceView, GameSettingsRaceRouter, GameSettingsRaceBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [GameSettingsRaceRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [GameSettingsRaceRouter].
     */
    override fun build(parentViewGroup: ViewGroup): GameSettingsRaceRouter {
        val view = createView(parentViewGroup)
        val interactor = GameSettingsRaceInteractor()
        val component = DaggerGameSettingsRaceBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.gamesettingsclassRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): GameSettingsRaceView {
        return GameSettingsRaceView(parentViewGroup.context)
    }

    interface ParentComponent : ActiveGameDependencyProvider

    @dagger.Module
    abstract class Module {

        @GameSettingsClassScope
        @Binds
        internal abstract fun presenter(view: GameSettingsRaceView): GameSettingsRacePresenter

        @dagger.Module
        companion object {

            @GameSettingsClassScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: GameSettingsRaceView,
                    interactor: GameSettingsRaceInteractor): GameSettingsRaceRouter {
                return GameSettingsRaceRouter(view, interactor, component)
            }

            @GameSettingsClassScope
            @Provides
            @JvmStatic
            fun viewModelProvider(
                    view: GameSettingsRaceView,
                    defaultSettingsRacesRepository: DefaultSettingRaceRepository,
                    game: Game,
                    stringRepository: StringRepository,
                    resourcesProvider: ResourcesProvider,
                    activityListener: ActivityListener,
                    activeGameEventRelay: Relay<ActiveGameEvent>,
                    gameRaceRepository: GameRaceRepository,
                    analyticsReporter: AnalyticsReporter
            ): GameSettingsRaceViewModelProvider {
                return GameSettingsRaceViewModelProviderImpl(
                        defaultSettingsRacesRepository,
                        game,
                        stringRepository,
                        resourcesProvider,
                        view,
                        activityListener,
                        activeGameEventRelay,
                        gameRaceRepository,
                        analyticsReporter)
            }
        }


    }

    @GameSettingsClassScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<GameSettingsRaceInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: GameSettingsRaceInteractor): Builder

            @BindsInstance
            fun view(view: GameSettingsRaceView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun gamesettingsclassRouter(): GameSettingsRaceRouter
    }

    @Scope
    @Retention(AnnotationRetention.BINARY)
    internal annotation class GameSettingsClassScope

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    internal annotation class GameSettingsClassInternal
}
