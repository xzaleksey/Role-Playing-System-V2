package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings

import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.adapter.GameSettingsListViewModel

class GameSettingsViewModelProviderImpl(
        private val stringRepository: StringRepository
) : GameSettingsViewModelProvider {

    override fun getSettingsViewModel(): GameSettingsViewModel {
        return GameSettingsViewModel(
                listOf(
                        GameSettingsListViewModel(
                                stringRepository.mainStats(),
                                GameSettingsViewModel.GameSettingsItemType.STATS
                        ),
                        GameSettingsListViewModel(
                                stringRepository.characterClasses(),
                                GameSettingsViewModel.GameSettingsItemType.CLASSES
                        )

                )
        )
    }
}

interface GameSettingsViewModelProvider {

    fun getSettingsViewModel(): GameSettingsViewModel
}