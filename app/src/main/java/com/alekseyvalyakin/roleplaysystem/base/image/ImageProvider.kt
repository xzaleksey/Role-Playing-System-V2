package com.alekseyvalyakin.roleplaysystem.base.image

import io.reactivex.Observable

interface ImageProvider {
    fun observeImage():Observable<ImageHolder>

    fun getId(): String
}