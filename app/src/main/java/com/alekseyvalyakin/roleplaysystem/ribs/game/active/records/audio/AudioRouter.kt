package com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.audio


import com.uber.rib.core.ViewRouter

/**
 * Adds and removes children of {@link PhotoBuilder.PhotoScope}.
 *
 */
class AudioRouter(
        view: AudioView,
        interactor: AudioInteractor,
        component: AudioBuilder.Component) : ViewRouter<AudioView, AudioInteractor, AudioBuilder.Component>(view, interactor, component)
