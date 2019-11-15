package com.alekseyvalyakin.roleplaysystem.ribs.license

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

class LicenseBuilder(dependency: ParentComponent) : BaseViewBuilder<LicenseView, LicenseBuilder.ParentComponent>(dependency) {

    override fun build(parentViewGroup: ViewGroup): LicenseRouter {
        val view = createView(parentViewGroup)
        val interactor = LicenseInteractor()
        val component = DaggerLicenseBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.licenseRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): LicenseView? {
        return LicenseView(parentViewGroup.context)
    }

    interface ParentComponent : RibDependencyProvider

    @dagger.Module
    abstract class Module {

        @LicenseScope
        @Binds
        internal abstract fun presenter(view: LicenseView): LicensePresenter

        @dagger.Module
        companion object {

            @LicenseScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: LicenseView,
                    interactor: LicenseInteractor): LicenseRouter {
                return LicenseRouter(view, interactor, component)
            }
        }

    }

    @LicenseScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<LicenseInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: LicenseInteractor): Builder

            @BindsInstance
            fun view(view: LicenseView): Builder

            fun parentComponent(component: ParentComponent): Builder

            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun licenseRouter(): LicenseRouter
    }

    @Scope
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class LicenseScope

    @Qualifier
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class LicenseInternal
}
