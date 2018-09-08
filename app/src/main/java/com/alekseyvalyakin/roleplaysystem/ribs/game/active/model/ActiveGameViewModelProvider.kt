package com.alekseyvalyakin.roleplaysystem.ribs.game.active.model

import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.base.image.ResourceImageHolderImpl
import com.alekseyvalyakin.roleplaysystem.base.model.BottomItem
import com.alekseyvalyakin.roleplaysystem.base.model.BottomPanelMenu
import com.alekseyvalyakin.roleplaysystem.base.model.NavigationId
import com.alekseyvalyakin.roleplaysystem.data.firestore.user.UserRepository
import com.alekseyvalyakin.roleplaysystem.data.game.Game
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository

class ActiveGameViewModelProviderImpl(
        private val game: Game,
        private val userRepository: UserRepository,
        private val stringRepository: StringRepository,
        private val resourcesProvider: ResourcesProvider
) : ActiveGameViewModelProvider {

    override fun getCurrentGame(): Game {
        return game
    }

    override fun getActiveGameViewModel(navigationId: NavigationId): ActiveGameViewModel {
        return ActiveGameViewModel(
                userRepository.isCurrentUser(game.masterId),
                bottomPanelMenu(navigationId))
    }

    private fun bottomPanelMenu(navigationId: NavigationId): BottomPanelMenu {
        val items = listOf(
                BottomItem(NavigationId.CHARACTERS,
                        stringRepository.getCharacters(),
                        ResourceImageHolderImpl(R.drawable.bottom_bar_characters, resourcesProvider)
                ),
                BottomItem(NavigationId.DICES,
                        stringRepository.getDices(),
                        ResourceImageHolderImpl(R.drawable.bottom_bar_dice, resourcesProvider)
                ),
                BottomItem(NavigationId.PICTURES,
                        stringRepository.getPictures(),
                        ResourceImageHolderImpl(R.drawable.bottom_bar_pictures, resourcesProvider)
                ),
                BottomItem(NavigationId.MENU,
                        stringRepository.getMenu(),
                        ResourceImageHolderImpl(R.drawable.bottom_bar_menu, resourcesProvider)
                ))
        var selectedIndex = items.indexOfFirst { navigationId == it.id }
        if (selectedIndex < 0) {
            selectedIndex = 0
        }
        return BottomPanelMenu(items, selectedIndex)
    }

}

interface ActiveGameViewModelProvider {
    fun getActiveGameViewModel(navigationId: NavigationId): ActiveGameViewModel

    fun getCurrentGame(): Game
}