package com.alekseyvalyakin.roleplaysystem.ribs.root

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.di.ActivityComponent
import com.alekseyvalyakin.roleplaysystem.ribs.auth.AuthBuilder
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link RootScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
class RootBuilder(dependency: ActivityComponent) : ViewBuilder<RootView, RootRouter, ActivityComponent>(dependency) {

    /**
     * Builds a new [RootRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [RootRouter].
     */
    fun build(parentViewGroup: ViewGroup): RootRouter {
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

    interface ParentComponent {

    }

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
            internal fun router(
                    component: Component,
                    view: RootView,
                    interactor: RootInteractor): RootRouter {
                return RootRouter(view, interactor, component, AuthBuilder(component))
            }
        }
    }

    @RootScope
    @dagger.Subcomponent(modules = [(Module::class)])
    interface Component : InteractorBaseComponent<RootInteractor>,
            BuilderComponent,
            AuthBuilder.ParentComponent {

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
