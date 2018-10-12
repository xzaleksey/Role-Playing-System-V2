package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos

import android.content.res.Configuration
import com.alekseyvalyakin.roleplaysystem.base.image.ImageData
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo.FireStoreIdPhoto
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo.FireStoreVisibility
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo.PhotoInGameRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGameDao
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGameUploadModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.adapter.FabState
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.adapter.PhotoFlexibleViewModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.adapter.PhotoViewModel
import com.alekseyvalyakin.roleplaysystem.utils.file.FileInfoProvider
import com.alekseyvalyakin.roleplaysystem.utils.getCommonDimen
import com.alekseyvalyakin.roleplaysystem.utils.getDisplayWidth
import com.alekseyvalyakin.roleplaysystem.utils.getScreenOrientation
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import java.io.File

class PhotoInGameViewModelProviderImpl(
        private val photoInGameRepository: PhotoInGameRepository,
        private val game: Game,
        private val photoInGameDao: PhotoInGameDao,
        private val resourcesProvider: ResourcesProvider,
        private val fileInfoProvider: FileInfoProvider,
        userRepository: UserRepository
) : PhotoInGameViewModelProvider {

    private val isMaster = userRepository.isCurrentUser(game.masterId)

    override fun observeViewModel(): Flowable<PhotoViewModel> {
        val size = calculateItemSize()
        return Flowable.combineLatest(getFirebasePhotosFlowable(), photoInGameDao.all(),
                BiFunction { fireStorePhotos: List<FireStoreIdPhoto>, photoUploadUploadModels: List<PhotoInGameUploadModel> ->
                    return@BiFunction PhotoViewModel(
                            fireStorePhotos.asSequence()
                                    .filter { isMaster || it.state.visibilityState == FireStoreVisibility.VISIBLE_TO_ALL.value }
                                    .map { photo ->
                                        val gameId = game.id
                                        val photoId = photo.id

                                        val imageProvider = PhotoInGameUrlProvider(photo.url,
                                                resourcesProvider,
                                                fileInfoProvider,
                                                gameId,
                                                photoId)

                                        PhotoFlexibleViewModel(photoId,
                                                imageProvider,
                                                photo.name,
                                                isMaster,
                                                photo.state.visibilityState == FireStoreVisibility.VISIBLE_TO_ALL.value,
                                                size,
                                                ImageData(photo.url,
                                                        File(fileInfoProvider.getPhotoInGameDirectory(gameId), photoId)
                                                )
                                        )
                                    }.toList(),
                            when {
                                !isMaster -> FabState.HIDDEN
                                isMaster && photoUploadUploadModels.isEmpty() -> FabState.VISIBLE
                                else -> FabState.LOADING
                            }
                    )
                })
    }

    private fun getFirebasePhotosFlowable(): Flowable<List<FireStoreIdPhoto>> {
        return photoInGameRepository.observeByDateCreate(game.id).startWith(emptyList<FireStoreIdPhoto>())
    }

    private fun calculateItemSize(): Int {
        var countOfPaddings = 2 + 2 * COLUMNS_COUNT
        var countOfColumns = COLUMNS_COUNT
        val context = resourcesProvider.getContext()

        if (context.getScreenOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
            countOfPaddings = 2 + 2 * COLUMNS_COUNT_LANDSCAPE
            countOfColumns = COLUMNS_COUNT_LANDSCAPE
        }

        return (context.getDisplayWidth() - context.getCommonDimen() * countOfPaddings) / countOfColumns
    }

    companion object {
        private const val COLUMNS_COUNT = 2
        private const val COLUMNS_COUNT_LANDSCAPE = 3
    }
}

interface PhotoInGameViewModelProvider {
    fun observeViewModel(): Flowable<PhotoViewModel>
}