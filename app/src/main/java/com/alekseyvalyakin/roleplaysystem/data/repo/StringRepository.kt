package com.alekseyvalyakin.roleplaysystem.data.repo

import com.alekseyvalyakin.roleplaysystem.R

class StringRepositoryImpl(private val resourcesProvider: ResourcesProvider) : StringRepository {
    override fun getCharacters(): String = resourcesProvider.getString(R.string.characters)
    override fun getGames(): String = resourcesProvider.getString(R.string.games)
    override fun getErrorNetwork(): String = resourcesProvider.getString(R.string.error_network_connection)
    override fun getProfile(): String = resourcesProvider.getString(R.string.profile)
    override fun getInputPassword(): String = resourcesProvider.getString(R.string.input_password)
    override fun getErrorIncorrectPassword(): String = resourcesProvider.getString(R.string.error_incorrect_password)
    override fun getCreateGame(): String = resourcesProvider.getString(R.string.create_game)
    override fun getMaster(): String = resourcesProvider.getString(R.string.master)
    override fun getMyLastGames(): String = resourcesProvider.getString(R.string.my_last_games)
    override fun getAllGames(): String = resourcesProvider.getString(R.string.all_games)
    override fun getMyProfile(): String = resourcesProvider.getString(R.string.my_profile)
    override fun getMax(): String = resourcesProvider.getString(R.string.max)
    override fun getDetails(): String = resourcesProvider.getString(R.string.details)
    override fun getError(): String = resourcesProvider.getString(R.string.error)
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
}
