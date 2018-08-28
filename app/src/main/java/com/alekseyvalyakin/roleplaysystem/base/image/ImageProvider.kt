package com.alekseyvalyakin.roleplaysystem.base.image

import com.alekseyvalyakin.roleplaysystem.utils.StringUtils
import io.reactivex.Observable

interface ImageProvider {
    fun observeImage(): Observable<ImageHolder>

    fun getId(): String

    object Empty : ImageProvider {
        override fun observeImage(): Observable<ImageHolder> {
            return Observable.empty()
        }

        override fun getId(): String {
            return StringUtils.EMPTY_STRING
        }
    }
}