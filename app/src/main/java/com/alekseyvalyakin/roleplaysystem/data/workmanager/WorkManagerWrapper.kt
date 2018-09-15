package com.alekseyvalyakin.roleplaysystem.data.workmanager

import androidx.work.*
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGame
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGameDao
import com.alekseyvalyakin.roleplaysystem.data.workmanager.PhotoInGameUpload.Companion.KEY_GAME_ID
import com.alekseyvalyakin.roleplaysystem.data.workmanager.PhotoInGameUpload.Companion.KEY_ID
import com.alekseyvalyakin.roleplaysystem.data.workmanager.PhotoInGameUpload.Companion.KEY_PATH_TO_FILE
import com.alekseyvalyakin.roleplaysystem.utils.createCompletable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class WorkManagerWrapperImpl(
        private val photoInGameDao: PhotoInGameDao
) : WorkManagerWrapper {
    private val workManager: WorkManager = WorkManager.getInstance()

    override fun startUploadPhotoInGameWork(photoInGame: PhotoInGame) {
        Timber.d("id" + photoInGame.id)
        val data = Data.Builder()
                .putAll(mapOf(
                        KEY_ID to photoInGame.id,
                        KEY_GAME_ID to photoInGame.gameId,
                        KEY_PATH_TO_FILE to photoInGame.filePath
                ))
                .build()

        val uploadPhoto = OneTimeWorkRequest.Builder(PhotoInGameUpload::class.java)
                .setInputData(data)
                .setConstraints(Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build())
                .build()

        createCompletable({
            photoInGame.workId = uploadPhoto.id.toString()
            photoInGameDao.update(photoInGame)
        }, Schedulers.io())

        workManager.enqueue(uploadPhoto)
    }
}

interface WorkManagerWrapper {
    fun startUploadPhotoInGameWork(photoInGame: PhotoInGame)
}

