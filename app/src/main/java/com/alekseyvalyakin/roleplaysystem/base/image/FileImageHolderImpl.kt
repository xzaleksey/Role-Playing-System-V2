package com.alekseyvalyakin.roleplaysystem.base.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import timber.log.Timber
import java.io.File
import java.io.FileInputStream


class FileImageHolderImpl(
        private val file: File
) : ImageHolder {

    override fun getDrawable(): Drawable? {
        return try {
            BitmapDrawable.createFromPath(file.absolutePath)
        } catch (t: Throwable) {
            Timber.e(t)
            null
        }
    }

    override fun getBitmap(): Bitmap? {
        FileInputStream(file).use {
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            Timber.d("get image from cache ${file.absolutePath}")
            return try {
                BitmapFactory.decodeStream(it, null, options)
            } catch (t: Throwable) {
                Timber.e(t)
                null
            }
        }
    }
}