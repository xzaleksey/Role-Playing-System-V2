package com.alekseyvalyakin.roleplaysystem.viewmodel.profile

import com.alekseyvalyakin.roleplaysystem.base.image.CompositeImageProviderImpl
import com.alekseyvalyakin.roleplaysystem.base.image.MaterialDrawableProviderImpl
import com.alekseyvalyakin.roleplaysystem.base.image.UrlRoundDrawableProviderImpl
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.flexible.profile.UserProfileViewModel
import com.alekseyvalyakin.roleplaysystem.flexible.subheader.SubHeaderViewModel
import eu.davidea.flexibleadapter.items.IFlexible
import io.reactivex.Flowable

class ProfileListViewModelProviderImpl(
        private val userRepository: UserRepository,
        private val resourceProvider: ResourcesProvider,
        private val stringRepository: StringRepository
) : ProfileListViewModelProvider {

    override fun getUserViewModelFlowable(): Flowable<List<IFlexible<*>>> {
        return userRepository.observeCurrentUser().map { user ->
            val userId = user.id
            val imageProvider = if (user.photoUrl.isBlank()) {
                MaterialDrawableProviderImpl(user.displayName, userId)
            } else {
                val url = user.photoUrl
                CompositeImageProviderImpl(
                        MaterialDrawableProviderImpl(user.displayName, userId),
                        UrlRoundDrawableProviderImpl(url, resourceProvider),
                        url
                )
            }
            return@map listOf(
                    SubHeaderViewModel(
                            stringRepository.getMyProfile(),
                            isDrawBottomDivider = true,
                            isDrawTopDivider = true),

                    UserProfileViewModel(
                            user.displayName,
                            user.email,
                            imageProvider,
                            userId,
                            true,
                            user
                    ))
        }
    }

}

interface ProfileListViewModelProvider {
    fun getUserViewModelFlowable(): Flowable<List<IFlexible<*>>>
}