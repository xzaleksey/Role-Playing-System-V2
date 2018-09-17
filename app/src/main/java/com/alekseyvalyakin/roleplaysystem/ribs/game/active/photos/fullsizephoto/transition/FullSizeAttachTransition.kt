package com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.fullsizephoto.transition

import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.fullsizephoto.FullSizePhotoBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.fullsizephoto.FullSizePhotoModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.fullsizephoto.FullSizePhotoRouter
import com.uber.rib.core.RouterNavigator
import com.uber.rib.core.RouterNavigatorState

class FullSizeAttachTransition<S : RouterNavigatorState>(
        private val fullSizePhotoBuilder: FullSizePhotoBuilder,
        private val fullSizePhotoModel: FullSizePhotoModel,
        private val viewGroup: ViewGroup
) : RouterNavigator.AttachTransition<FullSizePhotoRouter, S> {

    override fun buildRouter(): FullSizePhotoRouter {
        return fullSizePhotoBuilder.build(
                viewGroup,
                fullSizePhotoModel
        )
    }

    override fun willAttachToHost(router: FullSizePhotoRouter, previousState: S?, newState: S, isPush: Boolean) {
        viewGroup.addView(router.view)
    }
}