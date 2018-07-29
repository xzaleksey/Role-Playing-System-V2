package com.alekseyvalyakin.roleplaysystem.base.image

import io.reactivex.Observable

class CompositeImageProviderImpl(
        private val defaultImageProvider: ImageProvider,
        private val finalImageProvider: ImageProvider,
        id: String
) : DefaultImageProvider(id) {

    override fun observeImage(): Observable<ImageHolder> {
        return finalImageProvider.observeImage()
                .startWith(defaultImageProvider.observeImage())
    }
}