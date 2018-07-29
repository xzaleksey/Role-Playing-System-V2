package com.alekseyvalyakin.roleplaysystem.base.image

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider


class BitmapImageHolderImpl(
        private val bitmap: Bitmap,
        private val resourcesProvider: ResourcesProvider
) : ImageHolder {

    override fun getDrawable(): Drawable? {
        return resourcesProvider.bitmapToDrawable(bitmap)
    }

    override fun getBitmap(): Bitmap? {
        return bitmap
    }
}