package com.alekseyvalyakin.roleplaysystem.data.repo

import com.alekseyvalyakin.roleplaysystem.R

class StringRepositoryImpl(private val resourcesProvider: ResourcesProvider) : StringRepository {
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

    override fun description(): String {
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

    override fun mainCharacteristics(): String {
        return resourcesProvider.getString(R.string.main_characteristics)
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
    fun description(): String
    fun createGameDescriptionTitle(): String
    fun createGameDescriptionExample(): String
    fun createGamePasswordTitle(): String
    fun getLastGames(): String
    fun noName(): String
    fun noDescription(): String
    fun getDices(): String
    fun getPictures(): String
    fun getMenu(): String
    fun mainCharacteristics(): String
    fun characterClasses(): String
}
