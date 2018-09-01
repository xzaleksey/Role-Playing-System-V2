package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.di.rib.RibDependencyProvider
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link DiceScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
class DiceBuilder(dependency: ParentComponent) : ViewBuilder<DiceView, DiceRouter, DiceBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [DiceRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [DiceRouter].
     */
    fun build(parentViewGroup: ViewGroup): DiceRouter {
        val view = createView(parentViewGroup)
        val interactor = DiceInteractor()
        val component = DaggerDiceBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.diceRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): DiceView? {
        return DiceView(parentViewGroup.context)
    }

    interface ParentComponent : RibDependencyProvider

    @dagger.Module
    abstract class Module {

        @DiceScope
        @Binds
        internal abstract fun presenter(view: DiceView): DiceInteractor.DicePresenter

        @dagger.Module
        companion object {

            @DiceScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: DiceView,
                    interactor: DiceInteractor): DiceRouter {
                return DiceRouter(view, interactor, component)
            }
        }

    }

    @DiceScope
    @dagger.Component(modules = arrayOf(Module::class), dependencies = arrayOf(ParentComponent::class))
    interface Component : InteractorBaseComponent<DiceInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: DiceInteractor): Builder

            @BindsInstance
            fun view(view: DiceView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun diceRouter(): DiceRouter
    }

    @Scope
    @Retention(AnnotationRetention.BINARY)
    internal annotation class DiceScope

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    internal annotation class DiceInternal
}
