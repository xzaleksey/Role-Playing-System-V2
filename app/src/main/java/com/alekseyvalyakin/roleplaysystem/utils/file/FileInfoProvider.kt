package com.alekseyvalyakin.roleplaysystem.utils.file

import android.content.Context
import java.io.File

class FileInfoProviderImpl(
        val context: Context
) : FileInfoProvider {

    override fun getFilesPath(): File {
        return context.filesDir
    }

    override fun getImagesFilePath(): File {
        return File(getFilesPath(), "images")
    }

}

interface FileInfoProvider {
    fun getFilesPath(): File

    fun getImagesFilePath(): File
}