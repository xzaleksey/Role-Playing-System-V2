package com.alekseyvalyakin.roleplaysystem.base.image

import android.graphics.Typeface
import com.alekseyvalyakin.roleplaysystem.utils.ColorGenerator
import com.alekseyvalyakin.roleplaysystem.utils.dip
import io.reactivex.Observable

class MaterialDrawableProviderImpl(
        private val s: String,
        identifier: String
) : DefaultImageProvider(identifier) {

    override fun observeImage(): Observable<ImageHolder> {
        return Observable.fromCallable {
            DrawableImageHolderImpl(TextDrawable.builder().beginConfig()
                    .useFont(Typeface.SANS_SERIF)
                    .width(40.dip())
                    .height(40.dip())
                    .toUpperCase()
                    .endConfig()
                    .buildRound(s.substring(0, 1), ColorGenerator.MATERIAL.getColor(identifier)))
        }
    }
}