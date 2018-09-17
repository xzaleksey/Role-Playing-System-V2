package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.fullsizephoto

import com.alekseyvalyakin.roleplaysystem.base.image.ImageData
import java.io.Serializable

data class FullSizePhotoModel(
        val imageData: ImageData,
        val name: String
) : Serializable