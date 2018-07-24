package com.alekseyvalyakin.roleplaysystem.data.prefs

interface LocalKeyValueStorage {
    fun setLogin(login: String)

    fun getLogin(): String
}