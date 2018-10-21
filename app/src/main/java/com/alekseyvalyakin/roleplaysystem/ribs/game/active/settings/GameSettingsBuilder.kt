package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency.GameSettingsDependencyProvider
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.dependency.GameSettingsDependencyProviderImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.skills.GameSkillsRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.setting.def.stats.GameStatsRepository
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameDependencyProvider
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.classes.GameSettingsClassBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.dices.GameSettingsDicesBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.equip.GameSettingsEquipBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.races.GameSettingsRaceBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.skills.GameSettingsSkillsBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.spells.GameSettingsSpellsBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.stats.GameSettingsStatBuilder
import com.uber.rib.core.BaseViewBuilder
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.RouterNavigatorFactory
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
        internal abstract fun presenter(view: GameSettingsView): GameSettingsPresenter

        @dagger.Module
        companion object {

            @GameSettingsScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: GameSettingsView,
                    interactor: GameSettingsInteractor,
                    routerNavigatorFactory: RouterNavigatorFactory): GameSettingsRouter {
                return GameSettingsRouter(view,
                        interactor,
                        component,
                        GameSettingsStatBuilder(component),
                        GameSettingsClassBuilder(component),
                        GameSettingsSkillsBuilder(component),
                        GameSettingsRaceBuilder(component),
                        GameSettingsSpellsBuilder(component),
                        GameSettingsEquipBuilder(component),
                        GameSettingsDicesBuilder(component),
                        routerNavigatorFactory)
            }

            @GameSettingsScope
            @Provides
            @JvmStatic
            internal fun viewModelProvider(stringRepository: StringRepository, resourcesProvider: ResourcesProvider): GameSettingsViewModelProvider {
                return GameSettingsViewModelProviderImpl(stringRepository, resourcesProvider)
            }


            @GameSettingsScope
            @Provides
            @JvmStatic
            internal fun dependencyProvider(
                    gameStatsRepository: GameStatsRepository,
                    gameSkillsRepository: GameSkillsRepository,
                    resourcesProvider: ResourcesProvider): GameSettingsDependencyProvider {
                return GameSettingsDependencyProviderImpl(gameStatsRepository, gameSkillsRepository, resourcesProvider)
            }
        }

    }

    @GameSettingsScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<GameSettingsInteractor>,
            BuilderComponent,
            SettingsDependencyProvider,
            GameSettingsStatBuilder.ParentComponent,
            GameSettingsClassBuilder.ParentComponent,
            GameSettingsSkillsBuilder.ParentComponent,
            GameSettingsRaceBuilder.ParentComponent,
            GameSettingsSpellsBuilder.ParentComponent,
            GameSettingsEquipBuilder.ParentComponent,
            GameSettingsDicesBuilder.ParentComponent {

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
