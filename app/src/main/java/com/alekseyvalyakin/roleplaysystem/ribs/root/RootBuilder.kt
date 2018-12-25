package com.alekseyvalyakin.roleplaysystem.ribs.root

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityComponent
import com.alekseyvalyakin.roleplaysystem.di.rib.RibDependencyProvider
import com.alekseyvalyakin.roleplaysystem.ribs.auth.AuthBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.features.FeaturesBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.create.CreateGameListener
import com.alekseyvalyakin.roleplaysystem.ribs.license.LicenseBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.main.MainBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.main.MainRibListener
import com.alekseyvalyakin.roleplaysystem.ribs.profile.ProfileBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.profile.ProfileListener
import com.jakewharton.rxrelay2.PublishRelay
import com.uber.rib.core.BaseViewBuilder
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.RouterNavigatorFactory
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import io.reactivex.Observable
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link RootScope}.
 *
 */
class RootBuilder(dependency: ActivityComponent) : BaseViewBuilder<RootView, RootRouter, ActivityComponent>(dependency) {

    /**
     * Builds a new [RootRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [RootRouter].
     */
    override fun build(parentViewGroup: ViewGroup): RootRouter {
        val view = createView(parentViewGroup)
        val interactor = RootInteractor()
        val component = dependency.builder()
                .view(view)
                .interactor(interactor)
                .build()
        return component.rootRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): RootView? {
        return RootView(parentViewGroup.context)
    }

    interface ParentComponent : RibDependencyProvider

    @dagger.Module
    abstract class Module {

        @RootScope
        @Binds
        internal abstract fun presenter(view: RootView): RootInteractor.RootPresenter

        @dagger.Module
        companion object {

            @RootScope
            @Provides
            @JvmStatic
            fun router(
                    component: Component,
                    view: RootView,
                    interactor: RootInteractor
            ): RootRouter {
                return RootRouter(view, interactor, component,
                        AuthBuilder(component),
                        MainBuilder(component),
                        CreateGameBuilder(component),
                        ProfileBuilder(component),
                        ActiveGameBuilder(component),
                        FeaturesBuilder(component),
                        LicenseBuilder(component)
                )
            }

            @RootScope
            @Provides
            @JvmStatic
            fun mainRibRelay(): PublishRelay<MainRibListener.MainRibEvent> {
                return PublishRelay.create<MainRibListener.MainRibEvent>()
            }

            @RootScope
            @Provides
            @JvmStatic
            fun mainRibEventObservable(relay: PublishRelay<MainRibListener.MainRibEvent>): Observable<MainRibListener.MainRibEvent> {
                return relay
            }

            @RootScope
            @Provides
            @JvmStatic
            fun createGameRibListener(relay: PublishRelay<CreateGameListener.CreateGameEvent>): CreateGameListener {
                return object : CreateGameListener {
                    override fun onCreateGameEvent(createGameEvent: CreateGameListener.CreateGameEvent) {
                        relay.accept(createGameEvent)
                    }
                }
            }

            @RootScope
            @Provides
            @JvmStatic
            fun createGameRibRelay(): PublishRelay<CreateGameListener.CreateGameEvent> {
                return PublishRelay.create<CreateGameListener.CreateGameEvent>()
            }

            @RootScope
            @Provides
            @JvmStatic
            fun createGameRibEventObservable(relay: PublishRelay<CreateGameListener.CreateGameEvent>): Observable<CreateGameListener.CreateGameEvent> {
                return relay
            }

            @RootScope
            @Provides
            @JvmStatic
            fun profileListener(rootInteractor: RootInteractor): ProfileListener {
                return rootInteractor
            }

            @RootScope
            @Provides
            @JvmStatic
            fun mainRibListener(relay: PublishRelay<MainRibListener.MainRibEvent>): MainRibListener {
                return object : MainRibListener {
                    override fun onMainRibEvent(mainRibEvent: MainRibListener.MainRibEvent) {
                        relay.accept(mainRibEvent)
                    }
                }
            }
        }

    }

    @RootScope
    @dagger.Subcomponent(modules = [(Module::class)])
    interface Component : InteractorBaseComponent<RootInteractor>,
            BuilderComponent,
            RibDependencyProvider,
            AuthBuilder.ParentComponent,
            MainBuilder.ParentComponent,
            CreateGameBuilder.ParentComponent,
            ProfileBuilder.ParentComponent,
            ActiveGameBuilder.ParentComponent,
            FeaturesBuilder.ParentComponent, LicenseBuilder.ParentComponent {

        @dagger.Subcomponent.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: RootInteractor): Builder

            @BindsInstance
            fun view(view: RootView): Builder

            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun rootRouter(): RootRouter
    }

    @Scope
    @Retention(AnnotationRetention.BINARY)
    internal annotation class RootScope

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    internal annotation class RootInternal
}
