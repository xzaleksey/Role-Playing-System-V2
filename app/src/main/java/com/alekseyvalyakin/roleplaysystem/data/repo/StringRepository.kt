package com.alekseyvalyakin.roleplaysystem.data.repo

import com.alekseyvalyakin.roleplaysystem.R

class StringRepositoryImpl(private val resourcesProvider: ResourcesProvider) : StringRepository {
    override fun getNewRace(): String {
        return resourcesProvider.getString(R.string.new_race)
    }

    override fun getMyRace(): String {
        return resourcesProvider.getString(R.string.my_race)
    }

    override fun getRaces(): String {
        return resourcesProvider.getString(R.string.races)
    }

    override fun getMyClass(): String {
        return resourcesProvider.getString(R.string.my_class)
    }

    override fun getClasses(): String {
        return resourcesProvider.getString(R.string.classes)
    }

    override fun getNewClass(): String {
        return resourcesProvider.getString(R.string.new_class)
    }

    override fun getDicesChecks(): String {
        return resourcesProvider.getString(R.string.dices_checks)
    }

    override fun getEquipment(): String {
        return resourcesProvider.getString(R.string.equipment)
    }

    override fun getSpells(): String {
        return resourcesProvider.getString(R.string.spells)
    }

    override fun getSkills(): String {
        return resourcesProvider.getString(R.string.skills)
    }

    override fun getCharacterRaces(): String {
        return resourcesProvider.getString(R.string.character_races)
    }

    override fun getMyStat(): String {
        return resourcesProvider.getString(R.string.my_stat)
    }

    override fun getNewStat(): String {
        return resourcesProvider.getString(R.string.new_stat)
    }

    override fun getStats(): String {
        return resourcesProvider.getString(R.string.stats)
    }

    override fun getMenu(): String {
        return resourcesProvider.getString(R.string.menu)
    }

    override fun getPictures(): String {
        return resourcesProvider.getString(R.string.pictures)
    }

    override fun getDices(): String {
        return resourcesProvider.getString(R.string.dices)
    }

    override fun createGamePasswordTitle(): String {
        return resourcesProvider.getString(R.string.create_game_description_password)
    }

    override fun createGameDescriptionExample(): String {
        return resourcesProvider.getString(R.string.create_game_description_example)
    }

    override fun createGameDescriptionTitle(): String {
        return resourcesProvider.getString(R.string.create_game_description_title)
    }

    override fun getDescription(): String {
        return resourcesProvider.getString(R.string.description)
    }

    override fun getCharacters(): String {
        return resourcesProvider.getString(R.string.characters)
    }

    override fun getGames(): String {
        return resourcesProvider.getString(R.string.games)
    }

    override fun getErrorNetwork(): String {
        return resourcesProvider.getString(R.string.error_network_connection)
    }

    override fun getProfile(): String {
        return resourcesProvider.getString(R.string.profile)
    }

    override fun getInputPassword(): String {
        return resourcesProvider.getString(R.string.input_password)
    }

    override fun getErrorIncorrectPassword(): String {
        return resourcesProvider.getString(R.string.error_incorrect_password)
    }

    override fun getCreateGame(): String {
        return resourcesProvider.getString(R.string.create_game)
    }

    override fun getMaster(): String {
        return resourcesProvider.getString(R.string.master)
    }

    override fun getMyLastGames(): String = resourcesProvider.getString(R.string.my_last_games)

    override fun getLastGames(): String = resourcesProvider.getString(R.string.last_games)

    override fun getAllGames(): String {
        return resourcesProvider.getString(R.string.all_games)
    }

    override fun getMyProfile(): String {
        return resourcesProvider.getString(R.string.my_profile)
    }

    override fun getMax(): String {
        return resourcesProvider.getString(R.string.max)
    }

    override fun getDetails(): String {
        return resourcesProvider.getString(R.string.details)
    }

    override fun getError(): String {
        return resourcesProvider.getString(R.string.error)
    }

    override fun createGameNameTitle(): String {
        return resourcesProvider.getString(R.string.create_game_name_title)
    }

    override fun createGameStepText(currentStep: Int, maxSteps: Int): String {
        return resourcesProvider.getString(R.string.steps_format, currentStep, maxSteps)
    }

    override fun name(): String {
        return resourcesProvider.getString(R.string.name)
    }

    override fun createGameNameExample(): String {
        return resourcesProvider.getString(R.string.create_game_name_example)
    }

    override fun noName(): String {
        return resourcesProvider.getString(R.string.no_name)
    }

    override fun noDescription(): String {
        return resourcesProvider.getString(R.string.no_description)
    }

    override fun mainStats(): String {
        return resourcesProvider.getString(R.string.main_stats)
    }

    override fun characterClasses(): String {
        return resourcesProvider.getString(R.string.character_classes)
    }

}

interface StringRepository {
    fun getAllGames(): String
    fun getMyProfile(): String
    fun getMyLastGames(): String
    fun getMaster(): String
    fun getCreateGame(): String
    fun getErrorIncorrectPassword(): String
    fun getInputPassword(): String
    fun getProfile(): String
    fun getErrorNetwork(): String
    fun getGames(): String
    fun getCharacters(): String
    fun getMax(): String
    fun getDetails(): String
    fun getError(): String
    fun createGameNameTitle(): String
    fun createGameStepText(currentStep: Int, maxSteps: Int): String
    fun name(): String
    fun createGameNameExample(): String
    fun getDescription(): String
    fun createGameDescriptionTitle(): String
    fun createGameDescriptionExample(): String
    fun createGamePasswordTitle(): String
    fun getLastGames(): String
    fun noName(): String
    fun noDescription(): String
    fun getDices(): String
    fun getPictures(): String
    fun getMenu(): String
    fun mainStats(): String
    fun characterClasses(): String
    fun getStats(): String
    fun getNewStat(): String
    fun getNewClass(): String
    fun getMyStat(): String
    fun getCharacterRaces(): String
    fun getSkills(): String
    fun getSpells(): String
    fun getEquipment(): String
    fun getDicesChecks(): String
    fun getClasses(): String
    fun getMyClass(): String
    fun getRaces(): String
    fun getMyRace(): String
    fun getNewRace(): String
}
