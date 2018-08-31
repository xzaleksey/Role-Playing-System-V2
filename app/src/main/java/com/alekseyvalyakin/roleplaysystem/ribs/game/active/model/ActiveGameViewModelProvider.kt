package com.alekseyvalyakin.roleplaysystem.ribs.game.active.model

import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.base.image.ResourceImageHolderImpl
import com.alekseyvalyakin.roleplaysystem.base.model.BottomItem
import com.alekseyvalyakin.roleplaysystem.base.model.BottomPanelMenu
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
        return ActiveGameViewModel(BottomPanelMenu(listOf(
                BottomItem(R.id.bottom_menu_characters,
                        stringRepository.getCharacters(),
                        ResourceImageHolderImpl(R.drawable.bottom_bar_characters, resourcesProvider)
                ),
                BottomItem(R.id.bottom_menu_dices,
                        stringRepository.getDices(),
                        ResourceImageHolderImpl(R.drawable.bottom_bar_dice, resourcesProvider)
                ),
                BottomItem(R.id.bottom_menu_photos,
                        stringRepository.getPictures(),
                        ResourceImageHolderImpl(R.drawable.bottom_bar_pictures, resourcesProvider)
                ),
                BottomItem(R.id.bottom_menu_other,
                        stringRepository.getMenu(),
                        ResourceImageHolderImpl(R.drawable.bottom_bar_menu, resourcesProvider)
                )), 0))
    }

}

interface ActiveGameViewModelProvider {
    fun getActiveGameViewModel(): ActiveGameViewModel
}