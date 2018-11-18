package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.base.filter.FilterModel
import com.alekseyvalyakin.roleplaysystem.di.rib.RibDependencyProvider
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.ActiveGameDependencyProvider
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio.AudioBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.log.LogBuilder
import com.jakewharton.rxrelay2.BehaviorRelay
import com.uber.rib.core.BaseViewBuilder
import com.uber.rib.core.InteractorBaseComponent
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import javax.inject.Qualifier
import javax.inject.Scope

class RecordsBuilder(dependency: ParentComponent) : BaseViewBuilder<RecordsView, RecordsRouter, RecordsBuilder.ParentComponent>(dependency) {

    override fun build(parentViewGroup: ViewGroup): RecordsRouter {
        val view = createView(parentViewGroup)
        val interactor = RecordsInteractor()
        val component = DaggerRecordsBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .interactor(interactor)
                .build()
        return component.logRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): RecordsView? {
        return RecordsView(parentViewGroup.context)
    }

    interface ParentComponent : ActiveGameDependencyProvider

    interface RecordComponent : ActiveGameDependencyProvider {
        fun getSearchFilterFlowable(): Flowable<FilterModel>
    }

    @dagger.Module
    abstract class Module {

        @RecordsScope
        @Binds
        internal abstract fun presenter(view: RecordsView): RecordsPresenter

        @dagger.Module
        companion object {

            @RecordsScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: RecordsView,
                    interactor: RecordsInteractor): RecordsRouter {
                return RecordsRouter(view, interactor, component, LogBuilder(component), AudioBuilder(component))
            }

            @RecordsScope
            @Provides
            @JvmStatic
            internal fun searchFilterRelay(): BehaviorRelay<FilterModel> {
                return BehaviorRelay.createDefault(FilterModel())
            }

            @RecordsScope
            @Provides
            @JvmStatic
            internal fun searchFilterFlowable(behaviorRelay: BehaviorRelay<FilterModel>): Flowable<FilterModel> {
                return behaviorRelay.toFlowable(BackpressureStrategy.LATEST)
            }
        }
    }

    @RecordsScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<RecordsInteractor>, BuilderComponent,
            RecordComponent,
            LogBuilder.ParentComponent,
            AudioBuilder.ParentComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: RecordsInteractor): Builder

            @BindsInstance
            fun view(view: RecordsView): Builder

            fun parentComponent(component: ParentComponent): Builder
            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun logRouter(): RecordsRouter
    }

    @Scope
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class RecordsScope

    @Qualifier
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class RecordsInternal
}
