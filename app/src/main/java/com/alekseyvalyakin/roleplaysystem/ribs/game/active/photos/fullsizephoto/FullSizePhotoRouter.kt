package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.fullsizephoto

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link FullSizePhotoBuilder.FullSizePhotoScope}.
 *
 */
class FullSizePhotoRouter(
    view: FullSizePhotoView,
    interactor: FullSizePhotoInteractor,
    component: FullSizePhotoBuilder.Component) : ViewRouter<FullSizePhotoView, FullSizePhotoInteractor, FullSizePhotoBuilder.Component>(view, interactor, component)
