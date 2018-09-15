package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo.FireStorePhoto
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo.PhotoInGameRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import io.reactivex.Flowable

class PhotoInGameViewModelProviderImpl(
        private val photoInGameRepository: PhotoInGameRepository,
        private val game: Game,
        userRepository: UserRepository
) : PhotoInGameViewModelProvider {

    private val isMaster = userRepository.isCurrentUser(game.masterId)

    override fun observeViewModel(): Flowable<PhotoViewModel> {
        return getFlowable().map {
            return@map PhotoViewModel(
                    emptyList(),
                    isMaster
            )
        }
    }

    private fun getFlowable(): Flowable<List<FireStorePhoto>> {
        return photoInGameRepository.observeByDateCreate(game.id).startWith(emptyList<FireStorePhoto>())
    }
}

interface PhotoInGameViewModelProvider {
    fun observeViewModel(): Flowable<PhotoViewModel>
}