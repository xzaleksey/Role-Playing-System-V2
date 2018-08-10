package com.alekseyvalyakin.roleplaysystem.base.image

import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class UrlDrawableProviderImpl(
        private val url: String,
        private val resourcesProvider: ResourcesProvider,
        private val requestOptions: RequestOptions = RequestOptions()
) : DefaultImageProvider(url) {

    private val observable = Observable.create<ImageHolder> { emitter ->
        try {
            val bitmap = Glide.with(resourcesProvider.getContext())
                    .asBitmap()
                    .apply(requestOptions)
                    .load(url)
                    .submit()
                    .get()
            val bitmapImageHolderImpl: ImageHolder = BitmapImageHolderImpl(bitmap, resourcesProvider)
            if (!emitter.isDisposed) {
                emitter.onNext(bitmapImageHolderImpl)
                emitter.onComplete()
            }
        } catch (e: Exception) {
            if (!emitter.isDisposed) {
                emitter.onError(e)
            }
        }
    }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .share()

    override fun observeImage(): Observable<ImageHolder> {
        return observable
    }
}