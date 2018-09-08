package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameDependencyProvider
import com.uber.rib.core.BaseViewBuilder
import com.uber.rib.core.InteractorBaseComponent
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link DiceResultScope}.
 *
 */
class DiceResultBuilder(dependency: ParentComponent) : BaseViewBuilder<DiceResultView, DiceResultRouter, DiceResultBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [DiceResultRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [DiceResultRouter].
     */
    override fun build(parentViewGroup: ViewGroup): DiceResultRouter {
        val view = createView(parentViewGroup)
        val interactor = DiceResultInteractor()
        val component = DaggerDiceResultBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.diceResultRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): DiceResultView {
        return DiceResultView(parentViewGroup.context)
    }

    interface ParentComponent : ActiveGameDependencyProvider

    @dagger.Module
    abstract class Module {

        @DiceResultScope
        @Binds
        internal abstract fun presenter(view: DiceResultView): DiceResultInteractor.DiceResultPresenter

        @dagger.Module
        companion object {

            @DiceResultScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: DiceResultView,
                    interactor: DiceResultInteractor): DiceResultRouter {
                return DiceResultRouter(view, interactor, component)
            }
        }

    }

    @DiceResultScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<DiceResultInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: DiceResultInteractor): Builder

            @BindsInstance
            fun view(view: DiceResultView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun diceResultRouter(): DiceResultRouter
    }

    @Scope
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class DiceResultScope

    @Qualifier
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class DiceResultInternal
}
