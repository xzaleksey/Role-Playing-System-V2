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

    override fun getActiveGameViewModel(): ActiveGameViewModel {
        return ActiveGameViewModel(
                userRepository.isCurrentUser(game.masterId),
                bottomPanelMenu())
    }

    private fun bottomPanelMenu(): BottomPanelMenu {
        return BottomPanelMenu(listOf(
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
                )),
                0)
    }

}

interface ActiveGameViewModelProvider {
    fun getActiveGameViewModel(): ActiveGameViewModel
}