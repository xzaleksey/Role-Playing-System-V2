package com.alekseyvalyakin.roleplaysystem.data.workmanager

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.Uri
import androidx.work.Worker
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.app.RpsApp
import com.alekseyvalyakin.roleplaysystem.data.firestorage.FirebaseStorageRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.FirestoreCollection
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.GameRepository
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.photo.FireStoreIdPhoto
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGameDao
import com.alekseyvalyakin.roleplaysystem.utils.NotificationInteractor
import com.alekseyvalyakin.roleplaysystem.utils.file.FileInfoProvider
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageException
import com.rxfirebase2.DocumentNotExistsException
import com.rxfirebase2.RxFirestore
import id.zelory.compressor.Compressor
import timber.log.Timber
import java.io.File
import javax.inject.Inject


class PhotoInGameUploadWorker : Worker() {

    @Inject
    lateinit var photoInGameDao: PhotoInGameDao
    @Inject
    lateinit var fileInfoProvider: FileInfoProvider
    @Inject
    lateinit var gameRepository: GameRepository
    @Inject
    lateinit var notificationInteractor: NotificationInteractor
    @Inject
    lateinit var firebaseStorageRepository: FirebaseStorageRepository

    init {
        RpsApp.app.getAppComponent().inject(this)
    }

    @SuppressLint("CheckResult")
    override fun doWork(): Result {
        val dbId = inputData.getLong(KEY_ID, 0)
        val gameId = inputData.getString(KEY_GAME_ID)!!
        val pathToFile = inputData.getString(KEY_PATH_TO_FILE)!!
        val photosInGame = FirestoreCollection.PhotosInGame(gameId)
        val documentReference = photosInGame.getDbCollection().document()
        val photoId = documentReference.id

        val gameDocument = FirestoreCollection.GAMES.getDbCollection().document(gameId)
        val localFile = File(pathToFile)

        try {
            RxFirestore.getDocumentSingle(gameDocument).blockingGet()
        } catch (t: Throwable) {
            if (t is DocumentNotExistsException) {
                localFile.delete()
                photoInGameDao.deleteById(dbId)
                return Result.SUCCESS
            }
        }

        var result = Result.SUCCESS
        val notificationId = "$gameId/$photoId".hashCode()

        try {
            val compressedFile = createLocalFileCopy(localFile, gameId, photoId)

            try {
                val uri = firebaseStorageRepository.uploadPhotoInGameAndGetUri(
                        gameId, photoId, Uri.fromFile(compressedFile),
                        OnProgressListener { snapshot ->
                            Timber.d("Bytes transferred ${snapshot.bytesTransferred} Total bytes ${snapshot.totalByteCount}")
                            notificationInteractor.showProgressNotification(notificationId,
                                    RpsApp.app.getString(R.string.uploading_photo),
                                    snapshot.bytesTransferred,
                                    snapshot.totalByteCount)
                        }
                ).blockingGet()

                documentReference.set(FireStoreIdPhoto(
                        fileName = compressedFile.name,
                        url = uri.toString()))

                photoInGameDao.deleteById(dbId)
            } catch (t: Throwable) {
                result = if (t.cause is StorageException){
                    photoInGameDao.deleteById(dbId)
                    localFile.delete()
                    Result.FAILURE
                } else{
                    Result.RETRY
                }
                Timber.e(t)
            }

        } catch (t: Throwable) {
            Timber.e(t)
            photoInGameDao.deleteById(dbId)
            result = Result.FAILURE
        }

        notificationInteractor.dismissNotification(notificationId)
        Timber.d("title $dbId + gameId $gameId pathToFile $pathToFile")

        return result
    }

    private fun createLocalFileCopy(file: File, gameId: String, photoId: String): File {
        val newDirectory = fileInfoProvider.getPhotoInGameDirectory(gameId).absolutePath

        var compressedFile = File(newDirectory, photoId)
        if (compressedFile.exists()) {
            return compressedFile
        }

        val compressor = Compressor(RpsApp.app)
                .setDestinationDirectoryPath(newDirectory)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .setQuality(80)
        compressedFile = compressor.compressToFile(file, photoId)
        Timber.d("Before compress %s after compress %s", file.length(), compressedFile.length())
        file.delete()

        return compressedFile
    }

    companion object {
        const val KEY_ID = "id"
        const val KEY_GAME_ID = "game_id"
        const val KEY_PATH_TO_FILE = "path_to_file"
    }
}