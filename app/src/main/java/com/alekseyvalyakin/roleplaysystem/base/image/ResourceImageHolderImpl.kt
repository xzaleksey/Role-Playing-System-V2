package com.alekseyvalyakin.roleplaysystem.base.image

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider

class ResourceImageHolderImpl(
        @DrawableRes private val resId: Int,
        private val resourcesProvider: ResourcesProvider
) : ImageHolder {

    override fun getDrawable(): Drawable? {
        return resourcesProvider.getDrawable(resId)
    }

    override fun getBitmap(): Bitmap? {
        return resourcesProvider.getBitmap(resId)
    }
}