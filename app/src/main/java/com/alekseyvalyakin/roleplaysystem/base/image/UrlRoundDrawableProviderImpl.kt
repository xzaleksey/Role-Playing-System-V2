package com.alekseyvalyakin.roleplaysystem.base.image

import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.bumptech.glide.request.RequestOptions

class UrlRoundDrawableProviderImpl(
        url: String,
        resourcesProvider: ResourcesProvider
) : UrlDrawableProviderImpl(url, resourcesProvider, RequestOptions.circleCropTransform())