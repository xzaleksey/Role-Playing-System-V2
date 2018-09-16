package com.alekseyvalyakin.roleplaysystem.base.image

import android.graphics.Bitmap

interface UrlCacheProvider : ImageProvider {
    fun saveToCache(bitmap: Bitmap)

    fun existsInCache(): Boolean

    fun getFilePath(): String
}