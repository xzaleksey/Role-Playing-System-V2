package com.alekseyvalyakin.roleplaysystem.base.image

import android.graphics.Bitmap
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

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
            bitmap = getBitmap(urlCacheProvider, path)
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

    fun getBitmap(urlCacheProvider: UrlCacheProvider?, path: String): Bitmap {
        return if (urlCacheProvider != null) {
            if (urlCacheProvider.existsInCache()) {
                Timber.d("get photo from cache $path")
                getBitmapFromUrl(urlCacheProvider.getFilePath())
            } else {
                getBitmapFromUrl(path).apply {
                    urlCacheProvider.saveToCache(this)
                }
            }
        } else {
            getBitmapFromUrl(path)
        }
    }

    private fun getBitmapFromUrl(path: String): Bitmap {
        return Glide.with(resourcesProvider.getContext())
                .asBitmap()
                .apply(requestOptions)
                .load(path)
                .submit()
                .get()
    }

    override fun observeImage(): Observable<ImageHolder> {
        return observable
    }
}