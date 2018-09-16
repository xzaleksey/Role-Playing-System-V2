package com.alekseyvalyakin.roleplaysystem.utils

import android.graphics.Bitmap
import timber.log.Timber
import java.io.FileOutputStream


fun saveImage(fileName: String, bitmap: Bitmap) {
    try {
        FileOutputStream(fileName).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
    } catch (error: Exception) {
        Timber.e(error)
    }
}