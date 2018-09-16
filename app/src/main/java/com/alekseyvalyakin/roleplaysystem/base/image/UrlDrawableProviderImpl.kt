package com.alekseyvalyakin.roleplaysystem.base.image

import android.graphics.Bitmap
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class UrlDrawableProviderImpl(
        private val url: String,
        private val resourcesProvider: ResourcesProvider,
        private val requestOptions: RequestOptions = RequestOptions(),
        private val urlCacheProvider: UrlCacheProvider? = null
) : DefaultImageProvider(url) {

    private val observable = Observable.create<ImageHolder> { emitter ->
        try {
            val path: String = url
            val bitmap: Bitmap
            if (urlCacheProvider != null) {
                if (urlCacheProvider.existsInCache()) {
                    bitmap = getBitmapFromUrl(urlCacheProvider.getFilePath())
                } else {
                    bitmap = getBitmapFromUrl(path)
                    urlCacheProvider.saveToCache(bitmap)
                }
            } else {
                bitmap = getBitmapFromUrl(path)
            }

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

    private fun getBitmapFromUrl(path: String): Bitmap {
        val bitmap = Glide.with(resourcesProvider.getContext())
                .asBitmap()
                .apply(requestOptions)
                .load(path)
                .submit()
                .get()
        return bitmap
    }

    override fun observeImage(): Observable<ImageHolder> {
        return observable
    }
}