package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos

import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link PhotoBuilder.PhotoScope}.
 *
 */
class PhotoRouter(
        view: PhotoView,
        interactor: PhotoInteractor,
        component: PhotoBuilder.Component) : ViewRouter<PhotoView, PhotoInteractor, PhotoBuilder.Component>(view, interactor, component) {

}
