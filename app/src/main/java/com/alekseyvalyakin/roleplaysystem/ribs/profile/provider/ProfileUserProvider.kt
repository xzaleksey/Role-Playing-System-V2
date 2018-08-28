package com.alekseyvalyakin.roleplaysystem.ribs.profile.provider

import com.alekseyvalyakin.roleplaysystem.data.firestore.user.User
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable

class ProfileUserProviderImpl(
        private val user: User,
        private val userRepository: UserRepository
) : ProfileUserProvider {

    private val relay = BehaviorRelay.createDefault<User>(user)

    override fun onNameChanged(name: String): Completable {
        relay.accept(relay.value.copy(displayName = name))
        return userRepository.onDisplayNameChanged(name)
    }

    override fun observeCurrentUser(): Flowable<User> {
        return userRepository.observeCurrentUser()
                .startWith(user)
                .doOnNext { relay.accept(it) }
                .flatMap { relay.toFlowable(BackpressureStrategy.LATEST) }
    }

    override fun isCurrentUser(id: String): Boolean {
        return userRepository.isCurrentUser(id)
    }
}

interface ProfileUserProvider {
    fun observeCurrentUser(): Flowable<User>

    fun isCurrentUser(id: String): Boolean

    fun onNameChanged(name: String): Completable
}