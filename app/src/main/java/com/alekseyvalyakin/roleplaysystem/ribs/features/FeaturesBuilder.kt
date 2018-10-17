package com.alekseyvalyakin.roleplaysystem.ribs.features

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

class FeaturesBuilder(dependency: ParentComponent) : BaseViewBuilder<FeaturesView, FeaturesRouter, FeaturesBuilder.ParentComponent>(dependency) {

    override fun build(parentViewGroup: ViewGroup): FeaturesRouter {
        val view = createView(parentViewGroup)
        val interactor = FeaturesInteractor()
        val component = DaggerFeaturesBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.featuresRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): FeaturesView? {
        return FeaturesView(parentViewGroup.context)
    }

    interface ParentComponent : RibDependencyProvider

    @dagger.Module
    abstract class Module {

        @FeaturesScope
        @Binds
        internal abstract fun presenter(view: FeaturesView): FeaturesPresenter

        @dagger.Module
        companion object {

            @FeaturesScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: FeaturesView,
                    interactor: FeaturesInteractor): FeaturesRouter {
                return FeaturesRouter(view, interactor, component)
            }
        }

    }

    @FeaturesScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<FeaturesInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: FeaturesInteractor): Builder

            @BindsInstance
            fun view(view: FeaturesView): Builder

            fun parentComponent(component: ParentComponent): Builder

            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun featuresRouter(): FeaturesRouter
    }

    @Scope
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class FeaturesScope

    @Qualifier
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class FeaturesInternal
}
