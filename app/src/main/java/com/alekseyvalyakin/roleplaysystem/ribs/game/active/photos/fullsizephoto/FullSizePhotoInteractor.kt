package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.fullsizephoto

import com.alekseyvalyakin.roleplaysystem.base.image.CacheFileProvider
import com.alekseyvalyakin.roleplaysystem.base.image.UrlDrawableProviderImpl
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.uber.rib.core.BaseInteractor
import com.uber.rib.core.Bundle
import com.uber.rib.core.RibInteractor
import javax.inject.Inject

/**
 * Coordinates Business Logic for [FullSizePhotoScope].
 *
 */
@RibInteractor
class FullSizePhotoInteractor : BaseInteractor<FullSizePhotoInteractor.FullSizePhotoPresenter, FullSizePhotoRouter>() {

    @Inject
    lateinit var presenter: FullSizePhotoPresenter

    @Inject
    lateinit var fullSizePhotoModel: FullSizePhotoModel
    @Inject
    lateinit var resourcesProvider: ResourcesProvider


    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        val url = fullSizePhotoModel.imageData.url
        val urlCacheFileProvider = fullSizePhotoModel.imageData.localFile?.let {
            CacheFileProvider(url, it)
        }
        presenter.updateViewModel(
                FullSizePhotoViewModel(
                        UrlDrawableProviderImpl(
                                url,
                                resourcesProvider,
                                urlCacheProvider = urlCacheFileProvider),
                        fullSizePhotoModel.name
                )
        )
    }

    override fun willResignActive() {
        super.willResignActive()

    }

    /**
     * Presenter interface implemented by this RIB's view.
     */
    interface FullSizePhotoPresenter {
        fun updateViewModel(fullSizePhotoViewModel: FullSizePhotoViewModel)
    }
}
