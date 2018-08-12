package com.alekseyvalyakin.roleplaysystem.ribs.profile

import com.alekseyvalyakin.roleplaysystem.base.image.MaterialDrawableProviderImpl
import com.alekseyvalyakin.roleplaysystem.ribs.profile.provider.ProfileUserProvider
import io.reactivex.Flowable

class ProfileViewModelProviderImpl(
        private val profileUserProvider: ProfileUserProvider
) : ProfileViewModelProvider {

    override fun observeProfileViewModel(): Flowable<ProfileViewModel> {
        return profileUserProvider.observeCurrentUser()
                .map { user ->
                    val masterGames = user.countOfGamesMastered
                    val playedGames = user.countOfGamesPlayed
                    return@map ProfileViewModel(user.displayName,
                            user.email,
                            profileUserProvider.isCurrentUser(),
                            (masterGames + playedGames).toString(),
                            masterGames.toString(),
                            MaterialDrawableProviderImpl(user.displayName, user.id),
                            emptyList())
                }
    }
}

interface ProfileViewModelProvider {
    fun observeProfileViewModel(): Flowable<ProfileViewModel>
}