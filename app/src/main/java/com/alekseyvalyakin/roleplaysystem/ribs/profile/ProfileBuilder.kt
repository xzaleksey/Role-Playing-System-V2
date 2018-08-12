package com.alekseyvalyakin.roleplaysystem.ribs.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.User
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.di.rib.RibDependencyProvider
import com.alekseyvalyakin.roleplaysystem.ribs.profile.provider.ProfileUserProvider
import com.alekseyvalyakin.roleplaysystem.ribs.profile.provider.ProfileUserProviderImpl
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link ProfileScope}.
 *
 */
class ProfileBuilder(dependency: ParentComponent) : ViewBuilder<ProfileView, ProfileRouter, ProfileBuilder.ParentComponent>(dependency) {

    /**
     * Builds a new [ProfileRouter].
     *
     * @param parentViewGroup parent view group that this router's view will be added to.
     * @return a new [ProfileRouter].
     */
    fun build(parentViewGroup: ViewGroup, user: User): ProfileRouter {
        val view = createView(parentViewGroup)
        val interactor = ProfileInteractor()
        val component = DaggerProfileBuilder_Component.builder()
                .parentComponent(dependency)
                .view(view)
                .user(user)
                .interactor(interactor)
                .build()
        return component.profileRouter()
    }

    override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): ProfileView {
        return ProfileView(parentViewGroup.context)
    }

    interface ParentComponent : RibDependencyProvider

    @dagger.Module
    abstract class Module {

        @ProfileScope
        @Binds
        internal abstract fun presenter(view: ProfileView): ProfilePresenter

        @dagger.Module
        companion object {

            @ProfileScope
            @Provides
            @JvmStatic
            internal fun router(
                    component: Component,
                    view: ProfileView,
                    interactor: ProfileInteractor): ProfileRouter {
                return ProfileRouter(view, interactor, component)
            }

            @ProfileScope
            @Provides
            @JvmStatic
            internal fun profileUserProvider(user: User, userRepository: UserRepository): ProfileUserProvider {
                return ProfileUserProviderImpl(user, userRepository)
            }

            @ProfileScope
            @Provides
            @JvmStatic
            internal fun profileViewModelProvider(profileUserProvider: ProfileUserProvider): ProfileViewModelProvider {
                return ProfileViewModelProviderImpl(profileUserProvider)
            }
        }

    }

    @ProfileScope
    @dagger.Component(modules = [Module::class], dependencies = [ParentComponent::class])
    interface Component : InteractorBaseComponent<ProfileInteractor>, BuilderComponent {

        @dagger.Component.Builder
        interface Builder {
            @BindsInstance
            fun interactor(interactor: ProfileInteractor): Builder

            @BindsInstance
            fun view(view: ProfileView): Builder

            @BindsInstance
            fun user(user: User): Builder

            fun parentComponent(component: ParentComponent): Builder

            fun build(): Component
        }
    }

    interface BuilderComponent {
        fun profileRouter(): ProfileRouter
    }

    @Scope
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class ProfileScope

    @Qualifier
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class ProfileInternal
}
