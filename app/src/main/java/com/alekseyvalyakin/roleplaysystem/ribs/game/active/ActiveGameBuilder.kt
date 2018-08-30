package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import android.view.LayoutInflater
import android.view.ViewGroup
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.CLASS
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link ActiveGameScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
class ActiveGameBuilder(dependency: ParentComponent) : ViewBuilder<ActiveGameView, ActiveGameRouter, ActiveGameBuilder.ParentComponent>(dependency) {

  /**
   * Builds a new [ActiveGameRouter].
   *
   * @param parentViewGroup parent view group that this router's view will be added to.
   * @return a new [ActiveGameRouter].
   */
  fun build(parentViewGroup: ViewGroup): ActiveGameRouter {
    val view = createView(parentViewGroup)
    val interactor = ActiveGameInteractor()
    val component = DaggerActiveGameBuilder_Component.builder()
        .parentComponent(dependency)
        .view(view)
        .interactor(interactor)
        .build()
    return component.activegameRouter()
  }

  override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): ActiveGameView? {
    // TODO: Inflate a new view using the provided inflater, or create a new view programatically using the
    // provided context from the parentViewGroup.
    return null
  }

  interface ParentComponent {
    // TODO: Define dependencies required from your parent interactor here.
  }

  @dagger.Module
  abstract class Module {

    @ActiveGameScope
    @Binds
    internal abstract fun presenter(view: ActiveGameView): ActiveGameInteractor.ActiveGamePresenter

    @dagger.Module
    companion object {

      @ActiveGameScope
      @Provides
      @JvmStatic
      internal fun router(
          component: Component,
          view: ActiveGameView,
          interactor: ActiveGameInteractor): ActiveGameRouter {
        return ActiveGameRouter(view, interactor, component)
      }
    }

    // TODO: Create provider methods for dependencies created by this Rib. These should be static.
  }

  @ActiveGameScope
  @dagger.Component(modules = arrayOf(Module::class), dependencies = arrayOf(ParentComponent::class))
  interface Component : InteractorBaseComponent<ActiveGameInteractor>, BuilderComponent {

    @dagger.Component.Builder
    interface Builder {
      @BindsInstance
      fun interactor(interactor: ActiveGameInteractor): Builder

      @BindsInstance
      fun view(view: ActiveGameView): Builder

      fun parentComponent(component: ParentComponent): Builder
      fun build(): Component
    }
  }

  interface BuilderComponent {
    fun activegameRouter(): ActiveGameRouter
  }

  @Scope
  @Retention(CLASS)
  internal annotation class ActiveGameScope

  @Qualifier
  @Retention(CLASS)
  internal annotation class ActiveGameInternal
}
