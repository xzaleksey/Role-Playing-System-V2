package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo.FireStorePhoto
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo.PhotoInGameRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import io.reactivex.Flowable

class PhotoViewModelProviderImpl(
        private val photoInGameRepository: PhotoInGameRepository,
        private val game: Game,
        userRepository: UserRepository
) : PhotoViewModelProvider {

    private val isMaster = userRepository.isCurrentUser(game.masterId)

    override fun observeViewModel(): Flowable<PhotoViewModel> {
        return getFlowable().map {
            return@map PhotoViewModel(
                    emptyList(),
                    isMaster
            )
        }
    }

    fun getFlowable(): Flowable<List<FireStorePhoto>> {
        return photoInGameRepository.observeByDateCreate(game.id).startWith(emptyList<FireStorePhoto>())
    }
}

interface PhotoViewModelProvider {
    fun observeViewModel(): Flowable<PhotoViewModel>
}