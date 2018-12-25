package com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.dice.DicesRepository
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameDependencyProvider
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.diceresult.DiceResultBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.viewmodel.DiceViewModelProvider
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.viewmodel.DiceViewModelProviderImpl
import com.uber.rib.core.BaseViewBuilder
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.RouterNavigatorFactory
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
class DiceBuilder(dependency: ParentComponent) : BaseViewBuilder<DiceView, DiceRouter, DiceBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [DiceRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [DiceRouter].
     */
    override fun build(parentViewGroup: ViewGroup): DiceRouter {
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

    interface ParentComponent : ActiveGameDependencyProvider

    @dagger.Module
    abstract class Module {

        @DiceScope
        @Binds
        internal abstract fun presenter(view: DiceView): DicePresenter

        @dagger.Module
        companion object {

            @DiceScope
            @Provides
            @JvmStatic
            fun router(
                    component: Component,
                    view: DiceView,
                    interactor: DiceInteractor
            ): DiceRouter {
                return DiceRouter(view, interactor, component,
                        DiceResultBuilder(component))
            }

            @DiceScope
            @Provides
            @JvmStatic
            fun diceViewModelProvider(dicesRepository: DicesRepository, game: Game): DiceViewModelProvider {
                return DiceViewModelProviderImpl(dicesRepository, game)
            }
        }
    }

    @DiceScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<DiceInteractor>,
            BuilderComponent,
            DiceResultBuilder.ParentComponent {

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
