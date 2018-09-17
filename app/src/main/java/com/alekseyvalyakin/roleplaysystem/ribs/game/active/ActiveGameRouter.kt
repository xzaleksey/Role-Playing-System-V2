package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import com.alekseyvalyakin.roleplaysystem.base.model.NavigationId
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.DiceBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.DiceRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.model.ActiveGameViewModelProvider
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.PhotoBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.PhotoRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.fullsizephoto.FullSizePhotoBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.fullsizephoto.FullSizePhotoModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.fullsizephoto.FullSizePhotoRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.transition.ActiveGameInternalAttachTransition
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.transition.DefaultActiveGameInternalDetachTransition
import com.uber.rib.core.RestorableRouter
import com.uber.rib.core.RouterNavigatorFactory
import com.uber.rib.core.RouterNavigatorState
import com.uber.rib.core.ViewRouter
import java.io.Serializable

/**
 * Adds and removes children of {@link ActiveGameBuilder.ActiveGameScope}.
 *
 */
class ActiveGameRouter(
        view: ActiveGameView,
        interactor: ActiveGameInteractor,
        component: ActiveGameBuilder.Component,
        private val diceBuilder: DiceBuilder,
        private val routerNavigatorFactory: RouterNavigatorFactory,
        private val activeGameViewModelProvider: ActiveGameViewModelProvider,
        private val photoBuilder: PhotoBuilder,
        private val fullSizePhotoBuilder: FullSizePhotoBuilder
) : ViewRouter<ActiveGameView, ActiveGameInteractor, ActiveGameBuilder.Component>(view, interactor, component), RestorableRouter {

    private val modernRouter = routerNavigatorFactory.create<State>(this)
    private val dicesAttachTransition = ActiveGameInternalAttachTransition(diceBuilder, view)
    private val dicesDetachTransition = object : DefaultActiveGameInternalDetachTransition<DiceRouter, State>(view) {}

    private val photoAttachTransition = ActiveGameInternalAttachTransition(photoBuilder, view)
    private val photoDetachTransition = object : DefaultActiveGameInternalDetachTransition<PhotoRouter, State>(view) {}
    private var canBeClosed = false
    private var fullSizePhotoRouter: FullSizePhotoRouter? = null
    fun attachView(navigationId: NavigationId) {
        when (navigationId) {
            NavigationId.DICES -> {
                modernRouter.pushTransientState(State.DICES, dicesAttachTransition, dicesDetachTransition)
            }

            NavigationId.PICTURES -> {
                modernRouter.pushTransientState(State.PHOTOS, photoAttachTransition, photoDetachTransition)
            }
        }
    }

    fun attachFullSizePhoto(fullSizePhotoModel: FullSizePhotoModel) {
        if (fullSizePhotoRouter == null) {
            val fullScreenContainer = view.getFullScreenContainer()
            fullSizePhotoRouter = fullSizePhotoBuilder.build(fullScreenContainer, fullSizePhotoModel)
            attachChild(fullSizePhotoRouter)
            fullScreenContainer.addView(fullSizePhotoRouter!!.view)
        }
    }

    private fun detachFullSizePhoto(): Boolean {
        if (fullSizePhotoRouter != null) {
            val fullScreenContainer = view.getFullScreenContainer()
            detachChild(fullSizePhotoRouter)
            fullScreenContainer.removeView(fullSizePhotoRouter!!.view)
            fullSizePhotoRouter = null
            return true
        }

        return false
    }

    fun backPress(): Boolean {
        if (canBeClosed) {
            return false
        }

        if (modernRouter.peekState() == null) {
            return false
        }

        if (detachFullSizePhoto()) {
            return true
        }

        if (modernRouter.peekRouter()?.handleBackPress() == false) {
            return false
        }

        return true
    }

    fun onDelete() {
        canBeClosed = true
    }

    override fun getRestorableInfo(): Serializable? {
        return activeGameViewModelProvider.getCurrentGame()
    }

    data class State(val name: String) : RouterNavigatorState {

        override fun name(): String {
            return name
        }

        companion object {
            val DICES = State("DICES")
            val PHOTOS = State("PHOTOS")
        }
    }
}
