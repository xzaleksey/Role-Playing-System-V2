package com.alekseyvalyakin.roleplaysystem.data.workmanager

import androidx.work.*
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGameUploadModel
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGameDao
import com.alekseyvalyakin.roleplaysystem.data.workmanager.PhotoInGameUploadWorker.Companion.KEY_GAME_ID
import com.alekseyvalyakin.roleplaysystem.data.workmanager.PhotoInGameUploadWorker.Companion.KEY_ID
import com.alekseyvalyakin.roleplaysystem.data.workmanager.PhotoInGameUploadWorker.Companion.KEY_PATH_TO_FILE
import com.alekseyvalyakin.roleplaysystem.utils.createCompletable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class WorkManagerWrapperImpl(
        private val photoInGameDao: PhotoInGameDao
) : WorkManagerWrapper {
    private val workManager: WorkManager = WorkManager.getInstance()

    override fun startUploadPhotoInGameWork(photoInGameUploadModel: PhotoInGameUploadModel) {
        Timber.d("id" + photoInGameUploadModel.id)
        val data = Data.Builder()
                .putAll(mapOf(
                        KEY_ID to photoInGameUploadModel.id,
                        KEY_GAME_ID to photoInGameUploadModel.gameId,
                        KEY_PATH_TO_FILE to photoInGameUploadModel.filePath
                ))
                .build()

        val uploadPhoto = OneTimeWorkRequest.Builder(PhotoInGameUploadWorker::class.java)
                .setInputData(data)
                .setConstraints(Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build())
                .build()

        createCompletable({
            photoInGameUploadModel.workId = uploadPhoto.id.toString()
            photoInGameDao.update(photoInGameUploadModel)
        }, Schedulers.io())

        workManager.enqueue(uploadPhoto)
    }
}

interface WorkManagerWrapper {
    fun startUploadPhotoInGameWork(photoInGameUploadModel: PhotoInGameUploadModel)
}

