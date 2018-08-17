package com.alekseyvalyakin.roleplaysystem.base.image

import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class ObservableUrlDrawableProviderImpl(
        urlObservable: Observable<String>,
        id: String,
        private val resourcesProvider: ResourcesProvider,
        private val requestOptions: RequestOptions = RequestOptions()
) : DefaultImageProvider(id) {

    private val observable = urlObservable
            .observeOn(Schedulers.io())
            .concatMap { url ->
                try {
                    val bitmap = Glide.with(resourcesProvider.getContext())
                            .asBitmap()
                            .apply(requestOptions)
                            .load(url)
                            .submit()
                            .get()
                    val bitmapImageHolderImpl: ImageHolder = BitmapImageHolderImpl(bitmap, resourcesProvider)
                    return@concatMap Observable.just(bitmapImageHolderImpl)
                } catch (e: Exception) {
                    return@concatMap Observable.error<ImageHolder>(e)
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .share()

    override fun observeImage(): Observable<ImageHolder> {
        return observable
    }
}