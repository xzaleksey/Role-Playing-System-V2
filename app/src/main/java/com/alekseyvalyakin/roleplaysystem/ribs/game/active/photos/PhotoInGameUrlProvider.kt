package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos

import android.graphics.Bitmap
import com.alekseyvalyakin.roleplaysystem.base.image.FileImageHolderImpl
import com.alekseyvalyakin.roleplaysystem.base.image.ImageHolder
import com.alekseyvalyakin.roleplaysystem.base.image.UrlCacheProvider
import com.alekseyvalyakin.roleplaysystem.base.image.UrlDrawableProviderImpl
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.utils.file.FileInfoProvider
import com.alekseyvalyakin.roleplaysystem.utils.saveImage
import io.reactivex.Observable
import timber.log.Timber
import java.io.File

class PhotoInGameUrlProvider(
        url: String,
        resourcesProvider: ResourcesProvider,
        fileInfoProvider: FileInfoProvider,
        gameId: String,
        photoId: String
) : UrlDrawableProviderImpl(url, resourcesProvider,
        urlCacheProvider = object : UrlCacheProvider {

            override fun saveToCache(bitmap: Bitmap) {
                val file = getFile()
                Timber.d("Save image to cache ${file.absolutePath}")
                saveImage(file.absolutePath, bitmap)
            }

            override fun existsInCache(): Boolean {
                return getFile().exists()
            }

            override fun getFilePath(): String {
                return getFile().absolutePath
            }

            override fun observeImage(): Observable<ImageHolder> {
                return Observable.fromCallable<ImageHolder> {
                    val file = getFile()
                    if (!file.exists()) {
                        return@fromCallable ImageHolder.EMPTY
                    }
                    return@fromCallable FileImageHolderImpl(file)
                }
            }

            fun getFile() = File(fileInfoProvider.getPhotoInGameDirectory(gameId), photoId)

            override fun getId(): String {
                return photoId
            }

        })