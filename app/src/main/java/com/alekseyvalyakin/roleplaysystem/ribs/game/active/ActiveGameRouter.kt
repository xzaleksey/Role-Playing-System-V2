package com.alekseyvalyakin.roleplaysystem.ribs.game.active

import com.alekseyvalyakin.roleplaysystem.base.model.NavigationId
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.characters.GameCharactersBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.characters.GameCharactersRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.DiceBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.dice.DiceRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu.MenuBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.menu.MenuRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.model.ActiveGameViewModelProvider
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.PhotoBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.PhotoRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.fullsizephoto.FullSizePhotoBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.fullsizephoto.FullSizePhotoModel
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.photos.fullsizephoto.FullSizePhotoRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.RecordsBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.records.RecordsRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.GameSettingsBuilder
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.GameSettingsRouter
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.transition.BaseActiveGameInternalAttachTransition
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.transition.DefaultActiveGameInternalDetachTransition
import com.uber.rib.core.AttachInfo
import com.uber.rib.core.BaseRouter
import com.uber.rib.core.SerializableRouterNavigatorState

/**
 * Adds and removes children of {@link ActiveGameBuilder.ActiveGameScope}.
 *
 */
class ActiveGameRouter(
        view: ActiveGameView,
        interactor: ActiveGameInteractor,
        component: ActiveGameBuilder.Component,
        diceBuilder: DiceBuilder,
        private val activeGameViewModelProvider: ActiveGameViewModelProvider,
        photoBuilder: PhotoBuilder,
        gameSettingsBuilder: GameSettingsBuilder,
        private val fullSizePhotoBuilder: FullSizePhotoBuilder,
        gameCharactersBuilder: GameCharactersBuilder,
        recordsBuilder: RecordsBuilder,
        menuBuilder: MenuBuilder
) : BaseRouter<ActiveGameView, ActiveGameInteractor, ActiveGameRouter.State, ActiveGameBuilder.Component>(view, interactor, component) {

    private val dicesAttachTransition = BaseActiveGameInternalAttachTransition(diceBuilder, view)
    private val dicesDetachTransition = object : DefaultActiveGameInternalDetachTransition<DiceRouter, State>(view) {}

    private val photoAttachTransition = BaseActiveGameInternalAttachTransition(photoBuilder, view)
    private val photoDetachTransition = object : DefaultActiveGameInternalDetachTransition<PhotoRouter, State>(view) {}

    private val gameSettingsAttachTransition = BaseActiveGameInternalAttachTransition(gameSettingsBuilder, view)
    private val gameSettingsDetachTransition = object : DefaultActiveGameInternalDetachTransition<GameSettingsRouter, State>(view) {}

    private val gameCharactersAttachTransition = BaseActiveGameInternalAttachTransition(gameCharactersBuilder, view)
    private val gameCharacterssDetachTransition = object : DefaultActiveGameInternalDetachTransition<GameCharactersRouter, State>(view) {}

    private val gameLogAttachTransition = BaseActiveGameInternalAttachTransition(recordsBuilder, view)
    private val gameLogsDetachTransition = object : DefaultActiveGameInternalDetachTransition<RecordsRouter, State>(view) {}

    private val gameMenuAttachTransition = BaseActiveGameInternalAttachTransition(menuBuilder, view)
    private val gameMenuDetachTransition = object : DefaultActiveGameInternalDetachTransition<MenuRouter, State>(view) {}

    private var canBeClosed = false
    private var fullSizePhotoRouter: FullSizePhotoRouter? = null

    override fun initNavigator(navigator: MutableMap<String, (AttachInfo<State>) -> Boolean>) {
        navigator[DICES] = {
            internalPushRetainedState(State.DICES(), dicesAttachTransition, dicesDetachTransition)
        }
        navigator[PHOTOS] = {
            internalPushRetainedState(State.PHOTOS(), photoAttachTransition, photoDetachTransition)
        }
        navigator[SETTINGS] = {
            internalPushRetainedState(State.SETTINGS(), gameSettingsAttachTransition, gameSettingsDetachTransition)
        }
        navigator[MENU] = {
            internalPushRetainedState(State.MENU(), gameMenuAttachTransition, gameMenuDetachTransition)
        }
        navigator[CHARACTERS] = {
            internalPushRetainedState(State.CHARACTERS(), gameCharactersAttachTransition, gameCharacterssDetachTransition)
        }
        navigator[RECORDS] = {
            internalPushRetainedState(State.RECORDS(), gameLogAttachTransition, gameLogsDetachTransition)
        }
    }


    fun attachView(navigationId: NavigationId) {
        when (navigationId) {
            NavigationId.DICES -> {
                attachRib(AttachInfo(State.DICES(), false))
            }

            NavigationId.PHOTOS -> {
                attachRib(AttachInfo(State.PHOTOS(), false))
            }

            NavigationId.SETTINGS -> {
                attachRib(AttachInfo(State.SETTINGS(), false))
            }
            NavigationId.MENU -> {
                attachRib(AttachInfo(State.MENU(), false))
            }
            NavigationId.CHARACTERS -> {
                attachRib(AttachInfo(State.CHARACTERS(), false))
            }

            NavigationId.RECORDS -> {
                attachRib(AttachInfo(State.RECORDS(), false))
            }
            else -> {

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
            detachChild(fullSizePhotoRouter!!)
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

        if (peekState() == null) {
            return false
        }

        if (detachFullSizePhoto()) {
            return true
        }

        if (peekRouter()?.handleBackPress() == true) {
            return true
        }

        popState()

        return peekState() != null
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    fun setCanBeClosed() {
        canBeClosed = true
    }

    fun getCurrentNavigationId(): NavigationId {
        return peekState()?.navigationId ?: NavigationId.CHARACTERS
    }

    sealed class State(val name: String, val navigationId: NavigationId) : SerializableRouterNavigatorState {
        class DICES : State(DICES, NavigationId.DICES)
        class PHOTOS : State(PHOTOS, NavigationId.PHOTOS)
        class SETTINGS : State(SETTINGS, NavigationId.SETTINGS)
        class CHARACTERS : State(CHARACTERS, NavigationId.CHARACTERS)
        class RECORDS : State(RECORDS, NavigationId.RECORDS)
        class MENU : State(MENU, NavigationId.MENU)

        override fun name(): String {
            return name
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as State

            if (name != other.name) return false
            if (navigationId != other.navigationId) return false

            return true
        }

        override fun hashCode(): Int {
            var result = name.hashCode()
            result = 31 * result + navigationId.hashCode()
            return result
        }
    }

    companion object {
        const val DICES = "DICES"
        const val PHOTOS = "PHOTOS"
        const val SETTINGS = "SETTINGS"
        const val CHARACTERS = "CHARACTERS"
        const val RECORDS = "RECORDS"
        const val MENU = "MENU"
    }
}
