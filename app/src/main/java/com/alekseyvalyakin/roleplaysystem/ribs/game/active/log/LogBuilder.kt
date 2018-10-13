package com.alekseyvalyakin.roleplaysystem.ribs.game.active.log

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.log.LogRepository
import com.alekseyvalyakin.roleplaysystem.di.rib.RibDependencyProvider
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameDependencyProvider
import com.uber.rib.core.BaseViewBuilder
import com.uber.rib.core.InteractorBaseComponent
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope

class LogBuilder(dependency: ParentComponent) : BaseViewBuilder<LogView, LogRouter, LogBuilder.ParentComponent>(dependency) {

    override fun build(parentViewGroup: ViewGroup): LogRouter {
        val view = createView(parentViewGroup)
        val interactor = LogInteractor()
        val component = DaggerLogBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.logRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): LogView? {
        return LogView(parentViewGroup.context)
    }

    interface ParentComponent : ActiveGameDependencyProvider

    @dagger.Module
    abstract class Module {

        @LogScope
        @Binds
        internal abstract fun presenter(view: LogView): LogPresenter

        @dagger.Module
        companion object {

            @LogScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: LogView,
                    interactor: LogInteractor): LogRouter {
                return LogRouter(view, interactor, component)
            }

            @LogScope
            @Provides
            @JvmStatic
            internal fun logViewModelProvider(game: Game, logRepository: LogRepository): LogViewModelProvider {
                return LogViewModelProviderImpl(game, logRepository)
            }
        }
    }

    @LogScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<LogInteractor>, BuilderComponent,
            RibDependencyProvider {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: LogInteractor): Builder

            @BindsInstance
            fun view(view: LogView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun logRouter(): LogRouter
    }

    @Scope
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class LogScope

    @Qualifier
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class LogInternal
}
