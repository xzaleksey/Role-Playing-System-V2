package com.alekseyvalyakin.roleplaysystem.data.repo

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.support.v7.content.res.AppCompatResources
import com.alekseyvalyakin.roleplaysystem.utils.NumberUtils.ZERO
import com.alekseyvalyakin.roleplaysystem.utils.StringUtils.UNDEFINED
import timber.log.Timber

interface ResourcesProvider {
    @ColorInt
    fun getColor(@ColorRes colorRes: Int): Int

    fun getString(@StringRes stringRes: Int): String

    fun getDrawable(@DrawableRes drawableRes: Int): Drawable?
}

class ResourcesProviderImpl(private val context: Context) : ResourcesProvider {

    override fun getString(stringRes: Int): String {
        try {
            return context.getString(stringRes)
        } catch (ignored: Exception) {
            Timber.e(ignored)
        }
        return UNDEFINED
    }

    override fun getColor(colorRes: Int): Int {
        try {
            return ContextCompat.getColor(context, colorRes)
        } catch (ignored: Exception) {
            Timber.e(ignored)
        }
        return ZERO
    }

    override fun getDrawable(drawableRes: Int): Drawable? {
        try {
            return AppCompatResources.getDrawable(context, drawableRes)
        } catch (ignored: Exception) {
            Timber.e(ignored)
        }
        return null
    }

}