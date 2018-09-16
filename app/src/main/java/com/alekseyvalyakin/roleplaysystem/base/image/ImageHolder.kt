package com.alekseyvalyakin.roleplaysystem.base.image

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

interface ImageHolder {
    fun getDrawable(): Drawable?

    fun getBitmap(): Bitmap?

    object EMPTY : ImageHolder {
        override fun getDrawable(): Drawable? {
            return null
        }

        override fun getBitmap(): Bitmap? {
            return null
        }

    }
}