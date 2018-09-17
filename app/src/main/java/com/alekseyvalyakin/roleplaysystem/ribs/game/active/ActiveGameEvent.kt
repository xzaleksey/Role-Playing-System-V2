package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.fullsizephoto.FullSizePhotoModel

sealed class ActiveGameEvent {
    class OpenFullSizePhoto(val fullSizePhotoModel: FullSizePhotoModel) : ActiveGameEvent()
}