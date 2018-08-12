package com.alekseyvalyakin.roleplaysystem.ribs.profile.provider

import com.alekseyvalyakin.roleplaysystem.data.firestore.user.User
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import io.reactivex.Flowable

class ProfileUserProviderImpl(
        private val user: User,
        private val userRepository: UserRepository
) : ProfileUserProvider {

    override fun observeCurrentUser(): Flowable<User> {
        return userRepository.observeCurrentUser()
                .startWith(user)
    }

    override fun isCurrentUser(): Boolean {
        return userRepository.isCurrentUser(user.id)
    }
}

interface ProfileUserProvider {
    fun observeCurrentUser(): Flowable<User>

    fun isCurrentUser(): Boolean
}