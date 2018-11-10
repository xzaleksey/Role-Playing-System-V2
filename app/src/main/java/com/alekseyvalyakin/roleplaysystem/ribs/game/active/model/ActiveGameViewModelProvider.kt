package com.alekseyvalyakin.roleplaysystem.ribs.game.active.model

import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.base.image.ResourceImageHolderImpl
import com.alekseyvalyakin.roleplaysystem.base.model.BottomItem
import com.alekseyvalyakin.roleplaysystem.base.model.BottomPanelMenu
import com.alekseyvalyakin.roleplaysystem.base.model.NavigationId
import com.alekseyvalyakin.roleplaysystem.data.firestore.game.Game
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository

class ActiveGameViewModelProviderImpl(
        private val game: Game,
        private val userRepository: UserRepository,
        private val stringRepository: StringRepository,
        private val resourcesProvider: ResourcesProvider
) : ActiveGameViewModelProvider {

    override fun isMaster(): Boolean {
        return userRepository.isCurrentUser(game.masterId)
    }

    override fun getCurrentGame(): Game {
        return game
    }

    override fun getActiveGameViewModel(navigationId: NavigationId): ActiveGameViewModel {
        return ActiveGameViewModel(
                userRepository.isCurrentUser(game.masterId),
                bottomPanelMenu(navigationId))
    }

    private fun bottomPanelMenu(navigationId: NavigationId): BottomPanelMenu {
        val items = if (isMaster()) {
            getMasterItems()
        } else {
            getUserItems()
        }

        var selectedIndex = items.indexOfFirst { navigationId == it.id }
        if (selectedIndex < 0) {
            selectedIndex = 2
        }
        return BottomPanelMenu(items, selectedIndex)
    }

    private fun getMasterItems(): List<BottomItem> {
        return listOf(
                getCharacters(),
                getDices(),
                getRecords(),
                getSettings(),
                getMenu()
        )
    }

    private fun getUserItems(): List<BottomItem> {
        return listOf(
                getInfo(),
                getPhotos(),
                getCharacters(),
                getDices(),
                getMenu()
        )
    }

    private fun getMenu(): BottomItem {
        return BottomItem(NavigationId.MENU,
                stringRepository.getMenu(),
                ResourceImageHolderImpl(R.drawable.bottom_bar_menu, resourcesProvider)
        )
    }

    private fun getPhotos(): BottomItem {
        return BottomItem(NavigationId.PHOTOS,
                stringRepository.getPictures(),
                ResourceImageHolderImpl(R.drawable.bottom_bar_pictures, resourcesProvider)
        )
    }

    private fun getDices(): BottomItem {
        return BottomItem(NavigationId.DICES,
                stringRepository.getDices(),
                ResourceImageHolderImpl(R.drawable.bottom_bar_dice, resourcesProvider)
        )
    }

    private fun getCharacters(): BottomItem {
        return BottomItem(NavigationId.CHARACTERS,
                stringRepository.getCharacters(),
                ResourceImageHolderImpl(R.drawable.bottom_bar_characters, resourcesProvider)
        )
    }

    private fun getInfo(): BottomItem {
        return BottomItem(NavigationId.INFO,
                stringRepository.getCharacters(),
                ResourceImageHolderImpl(R.drawable.bottom_bar_info, resourcesProvider)
        )
    }

    private fun getSettings(): BottomItem {
        return BottomItem(NavigationId.SETTINGS,
                stringRepository.getSettings(),
                ResourceImageHolderImpl(R.drawable.bottom_bar_settings, resourcesProvider)
        )
    }

    private fun getRecords(): BottomItem {
        return BottomItem(NavigationId.RECORDS,
                stringRepository.getRecords(),
                ResourceImageHolderImpl(R.drawable.bottom_bar_records, resourcesProvider)
        )
    }

}

interface ActiveGameViewModelProvider {
    fun getActiveGameViewModel(navigationId: NavigationId): ActiveGameViewModel

    fun getCurrentGame(): Game

    fun isMaster(): Boolean
}