package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos

import com.alekseyvalyakin.roleplaysystem.base.image.CacheFileProvider
import com.alekseyvalyakin.roleplaysystem.base.image.UrlDrawableProviderImpl
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.utils.file.FileInfoProvider
import java.io.File

class PhotoInGameUrlProvider(
        url: String,
        resourcesProvider: ResourcesProvider,
        fileInfoProvider: FileInfoProvider,
        gameId: String,
        photoId: String
) : UrlDrawableProviderImpl(url, resourcesProvider,
        urlCacheProvider = CacheFileProvider(
                url,
                File(fileInfoProvider.getPhotoInGameDirectory(gameId), photoId))
)