package com.alekseyvalyakin.roleplaysystem.data.remoteconfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import timber.log.Timber


class RemoteConfigProviderImpl : RemoteConfigProvider {

    private val remoteConfig = FirebaseRemoteConfig.getInstance()

    override fun fetch() {
        remoteConfig.fetch().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                remoteConfig.activateFetched()
            } else {
                Timber.e("Remote config fetch failed")
            }
        }
    }

    override fun isFetched(): Boolean {
        return remoteConfig.info.lastFetchStatus != 0
    }

    override fun getWelcomeMessage(): String {
        return remoteConfig.getString(WELCOME_MESSAGE)
    }

    companion object {
        private const val WELCOME_MESSAGE = "welcome_message"
    }
}

interface RemoteConfigProvider {
    fun fetch()

    fun getWelcomeMessage(): String

    fun isFetched(): Boolean
}