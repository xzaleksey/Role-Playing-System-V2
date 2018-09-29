package com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings

import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.data.repo.ResourcesProvider
import com.alekseyvalyakin.roleplaysystem.data.repo.StringRepository
import com.alekseyvalyakin.roleplaysystem.ribs.game.active.settings.adapter.GameSettingsListViewModel

class GameSettingsViewModelProviderImpl(
        private val stringRepository: StringRepository,
        private val resourcesProvider: ResourcesProvider
) : GameSettingsViewModelProvider {

    override fun getSettingsViewModel(): GameSettingsViewModel {
        return GameSettingsViewModel(
                listOf(
                        GameSettingsListViewModel(
                                stringRepository.mainStats(),
                                GameSettingsViewModel.GameSettingsItemType.STATS,
                                resourcesProvider.getDrawable(R.drawable.ic_main_stats)
                        ),
                        GameSettingsListViewModel(
                                stringRepository.characterClasses(),
                                GameSettingsViewModel.GameSettingsItemType.CLASSES,
                                resourcesProvider.getDrawable(R.drawable.ic_classes)
                        ),
                        GameSettingsListViewModel(
                                stringRepository.getCharacterRaces(),
                                GameSettingsViewModel.GameSettingsItemType.RACES,
                                resourcesProvider.getDrawable(R.drawable.ic_races)
                        ),
                        GameSettingsListViewModel(
                                stringRepository.getSkills(),
                                GameSettingsViewModel.GameSettingsItemType.SKILLS,
                                resourcesProvider.getDrawable(R.drawable.ic_skills)
                        ),
                        GameSettingsListViewModel(
                                stringRepository.getSpells(),
                                GameSettingsViewModel.GameSettingsItemType.SPELLS,
                                resourcesProvider.getDrawable(R.drawable.ic_spells)
                        ),
                        GameSettingsListViewModel(
                                stringRepository.getEquipment(),
                                GameSettingsViewModel.GameSettingsItemType.EQUIPMENT,
                                resourcesProvider.getDrawable(R.drawable.ic_equiment)
                        ),
                        GameSettingsListViewModel(
                                stringRepository.getDicesChecks(),
                                GameSettingsViewModel.GameSettingsItemType.DICES,
                                resourcesProvider.getDrawable(R.drawable.ic_dices)
                        )


                )
        )
    }
}

interface GameSettingsViewModelProvider {

    fun getSettingsViewModel(): GameSettingsViewModel
}