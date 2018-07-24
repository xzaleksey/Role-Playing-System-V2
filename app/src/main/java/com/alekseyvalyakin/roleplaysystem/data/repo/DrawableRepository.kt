package com.alekseyvalyakin.roleplaysystem.data.repo

import android.graphics.drawable.Drawable
import com.alekseyvalyakin.roleplaysystem.R

class DrawableRepositoryImpl(private val resourcesProvider: ResourcesProvider) : DrawableRepository {
    override fun getMageIcon(): Drawable? = resourcesProvider.getDrawable(R.drawable.mage)

    override fun getProfileIcon(): Drawable? = resourcesProvider.getDrawable(R.drawable.profile_icon)
}

interface DrawableRepository {
    fun getProfileIcon(): Drawable?
    fun getMageIcon(): Drawable?
}
