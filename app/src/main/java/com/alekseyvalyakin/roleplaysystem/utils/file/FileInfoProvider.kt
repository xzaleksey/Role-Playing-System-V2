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

    override fun getPhotoInGameDirectory(gameId: String): File {
        return File(getImagesFilePath(), "${Game.STORAGE_KEY}/${PhotoInGameUploadModel.STORAGE_KEY}/$gameId")
    }

    override fun getFilesPath(): File {
        return File(FileUtils.getExternalFilesDir(null, context))
    }

    override fun getImagesFilePath(): File {
        return File(FileUtils.getExternalFilesDir(Environment.DIRECTORY_PICTURES, context))
    }

}

interface FileInfoProvider {
    fun getFilesPath(): File

    fun getImagesFilePath(): File

    fun getPhotoInGameDirectory(gameId: String): File
}