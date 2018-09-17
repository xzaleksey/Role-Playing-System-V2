package com.alekseyvalyakin.roleplaysystem.base.image

import android.graphics.Bitmap
import com.alekseyvalyakin.roleplaysystem.utils.saveImage
import io.reactivex.Observable
import timber.log.Timber
import java.io.File

class CacheFileProvider(
        private val fileId: String,
        private val file: File
) : UrlCacheProvider {

    override fun saveToCache(bitmap: Bitmap) {
        Timber.d("Save image to cache ${file.absolutePath}")
        saveImage(file.absolutePath, bitmap)
    }

    override fun existsInCache(): Boolean {
        return file.exists()
    }

    override fun getFilePath(): String {
        return file.absolutePath
    }

    override fun observeImage(): Observable<ImageHolder> {
        return Observable.fromCallable<ImageHolder> {
            if (!file.exists()) {
                return@fromCallable ImageHolder.EMPTY
            }
            return@fromCallable FileImageHolderImpl(file)
        }
    }

    override fun getId(): String {
        return fileId
    }
}