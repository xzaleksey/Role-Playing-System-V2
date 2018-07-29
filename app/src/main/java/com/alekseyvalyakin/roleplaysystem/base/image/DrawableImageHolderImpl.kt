package com.alekseyvalyakin.roleplaysystem.base.image

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.alekseyvalyakin.roleplaysystem.utils.toBitmap

class DrawableImageHolderImpl(
        private val drawable: Drawable
) : ImageHolder {

    override fun getDrawable(): Drawable? {
        return drawable
    }

    override fun getBitmap(): Bitmap? {
        return drawable.toBitmap()
    }
}