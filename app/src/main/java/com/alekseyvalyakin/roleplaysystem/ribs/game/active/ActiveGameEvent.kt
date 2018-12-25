package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.fullsizephoto.FullSizePhotoModel

sealed class ActiveGameEvent {
    class OpenFullSizePhoto(val fullSizePhotoModel: FullSizePhotoModel) : ActiveGameEvent()

     object HideBottomBar : ActiveGameEvent()
     object ShowBottomBar : ActiveGameEvent()
     object CloseActiveGame : ActiveGameEvent()
     object NavigateToPhotos : ActiveGameEvent()
}