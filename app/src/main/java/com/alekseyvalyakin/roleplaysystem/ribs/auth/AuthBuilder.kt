package com.alekseyvalyakin.roleplaysystem.ribs.auth

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.di.rib.RibDependencyProvider
import com.uber.rib.core.BaseViewBuilder
import com.uber.rib.core.InteractorBaseComponent
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link AuthScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
class AuthBuilder(dependency: ParentComponent) : BaseViewBuilder<AuthView, AuthBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [AuthRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [AuthRouter].
     */
    override fun build(parentViewGroup: ViewGroup): AuthRouter {
        val view = createView(parentViewGroup)
        val interactor = AuthInteractor()
        val component = DaggerAuthBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.authRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): AuthView {
        return AuthView(parentViewGroup.context)
    }

    interface ParentComponent : RibDependencyProvider {

    }

    @dagger.Module
    abstract class Module {

        @AuthScope
        @Binds
        internal abstract fun presenter(view: AuthView): AuthInteractor.AuthPresenter

        @dagger.Module
        companion object {

            @AuthScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: AuthView,
                    interactor: AuthInteractor): AuthRouter {
                return AuthRouter(view, interactor, component)
            }
        }

    }

    @AuthScope
    @dagger.Component(modules = [(Module::class)], dependencies = [(ParentComponent::class)])
    interface Component : InteractorBaseComponent<AuthInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: AuthInteractor): Builder

            @BindsInstance
            fun view(view: AuthView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun authRouter(): AuthRouter
    }

    @Scope
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class AuthScope

    @Qualifier
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class AuthInternal
}
