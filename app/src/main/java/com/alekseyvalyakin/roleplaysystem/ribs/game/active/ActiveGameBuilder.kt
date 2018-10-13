package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.di.rib.RibDependencyProvider
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.characters.GameCharactersBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.DiceBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.log.LogBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.model.ActiveGameViewModelProvider
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.model.ActiveGameViewModelProviderImpl
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.PhotoBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.fullsizephoto.FullSizePhotoBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.GameSettingsBuilder
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.RouterNavigatorFactory
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import io.reactivex.Observable
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link ActiveGameScope}.
 *
 * Scope in active game
 */
class ActiveGameBuilder(dependency: ParentComponent) : ViewBuilder<ActiveGameView, ActiveGameRouter, ActiveGameBuilder.ParentComponent>(dependency) {

    /**f
     * Builds a new [ActiveGameRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [ActiveGameRouter].
     */
    fun build(parentViewGroup: ViewGroup, game: Game): ActiveGameRouter {
        val view = createView(parentViewGroup)
        val interactor = ActiveGameInteractor()
        val component = DaggerActiveGameBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .game(game)
                .interactor(interactor)
                .build()
        return component.activeGameRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): ActiveGameView? {
        return ActiveGameView(parentViewGroup.context)
    }

    interface ParentComponent : RibDependencyProvider

    @dagger.Module
    abstract class Module {

        @ActiveGameScope
        @Binds
        internal abstract fun presenter(view: ActiveGameView): ActiveGamePresenter

        @dagger.Module
        companion object {

            @ActiveGameScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: ActiveGameView,
                    interactor: ActiveGameInteractor,
                    routerNavigatorFactory: RouterNavigatorFactory,
                    activeGameViewModelProvider: ActiveGameViewModelProvider,
                    relay: Relay<ActiveGameEvent>): ActiveGameRouter {

                return ActiveGameRouter(view,
                        interactor,
                        component,
                        DiceBuilder(component),
                        routerNavigatorFactory,
                        activeGameViewModelProvider,
                        PhotoBuilder(component),
                        GameSettingsBuilder(component),
                        FullSizePhotoBuilder(component),
                        GameCharactersBuilder(component),
                        LogBuilder(component))
            }

            @ActiveGameScope
            @Provides
            @JvmStatic
            internal fun viewModelProvider(game: Game,
                                           userRepository: UserRepository,
                                           stringRepository: StringRepository,
                                           resourcesProvider: ResourcesProvider): ActiveGameViewModelProvider {
                return ActiveGameViewModelProviderImpl(game, userRepository, stringRepository, resourcesProvider)
            }

            @ActiveGameScope
            @Provides
            @JvmStatic
            internal fun activeGameRelay(): Relay<ActiveGameEvent> {
                return PublishRelay.create()
            }

            @ActiveGameScope
            @Provides
            @JvmStatic
            internal fun activeGameEventObservable(relay: Relay<ActiveGameEvent>): Observable<ActiveGameEvent> {
                return relay
            }
        }

    }

    @ActiveGameScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<ActiveGameInteractor>,
            BuilderComponent,
            DiceBuilder.ParentComponent,
            ActiveGameDependencyProvider,
            PhotoBuilder.ParentComponent,
            FullSizePhotoBuilder.ParentComponent,
            GameSettingsBuilder.ParentComponent,
            GameCharactersBuilder.ParentComponent,
            LogBuilder.ParentComponent {
        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: ActiveGameInteractor): Builder

            @BindsInstance
            fun view(view: ActiveGameView): Builder

            @BindsInstance
            fun game(game: Game): Builder

            fun parentComponent(component: ParentComponent): Builder

            fun build(): Component

        }
    }

    interface BuilderComponent {
        fun activeGameRouter(): ActiveGameRouter
    }

    @Scope
    @Retention(AnnotationRetention.BINARY)
    internal annotation class ActiveGameScope

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    internal annotation class ActiveGameInternal
}
