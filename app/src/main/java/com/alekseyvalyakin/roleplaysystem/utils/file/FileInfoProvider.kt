package com.alekseyvalyakin.roleplaysystem.utils.file

import android.content.Context
import android.os.Environment
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.room.game.photo.PhotoInGameUploadModel
import com.kbeanie.multipicker.utils.FileUtils
import java.io.File

class FileInfoProviderImpl(
        val context: Context
) : FileInfoProvider {
    override fun getAvatarsPath(): File {
        return File(getImagesFilePath(), "avatars")
    }

    override fun getPhotoInGameDirectory(gameId: String): File {
        return File(getImagesFilePath(), "${Game.STORAGE_KEY}/${PhotoInGameUploadModel.STORAGE_KEY}/$gameId")
    }

    override fun getFilesPath(): File {
        return File(FileUtils.getExternalFilesDir(null, context))
    }

    override fun getImagesFilePath(): File {
        return File(FileUtils.getExternalFilesDir(Environment.DIRECTORY_PICTURES, context))
    }

    override fun getRecordsTempDir(): File {
        return File(FileUtils.getExternalFilesDir(Environment.DIRECTORY_MUSIC, context))
    }

    override fun getRecordsDir(): File {
        return File(Environment.getExternalStorageDirectory(), "Rpg assistant")
    }

}

interface FileInfoProvider {
    fun getFilesPath(): File

    fun getImagesFilePath(): File

    fun getAvatarsPath(): File

    fun getPhotoInGameDirectory(gameId: String): File

    fun getRecordsTempDir(): File

    fun getRecordsDir(): File
}