package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos

import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo.FireStorePhoto
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo.PhotoInGameRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGame
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGameDao
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction

class PhotoInGameViewModelProviderImpl(
        private val photoInGameRepository: PhotoInGameRepository,
        private val game: Game,
        private val photoInGameDao: PhotoInGameDao,
        userRepository: UserRepository
) : PhotoInGameViewModelProvider {

    private val isMaster = userRepository.isCurrentUser(game.masterId)

    override fun observeViewModel(): Flowable<PhotoViewModel> {
        return Flowable.combineLatest(getFirebasePhotosFlowable(), photoInGameDao.all(),
                BiFunction { fireStorePhotos: List<FireStorePhoto>, photoUploads: List<PhotoInGame> ->
                    return@BiFunction PhotoViewModel(
                            emptyList(),
                            when {
                                !isMaster -> FabState.HIDDEN
                                isMaster && photoUploads.isEmpty() -> FabState.VISIBLE
                                else -> FabState.LOADING
                            }
                    )
                })
    }

    private fun getFirebasePhotosFlowable(): Flowable<List<FireStorePhoto>> {
        return photoInGameRepository.observeByDateCreate(game.id).startWith(emptyList<FireStorePhoto>())
    }
}

interface PhotoInGameViewModelProvider {
    fun observeViewModel(): Flowable<PhotoViewModel>
}