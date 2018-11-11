package com.alekseyvalyakin.roleplaysystem.app

import com.alekseyvalyakin.roleplaysystem.base.observer.DisposableObserver

class AppSubscriptionManager(
        private val recordSoundObserver: DisposableObserver
) {
    fun subscribe() {
        recordSoundObserver.subscribe()
    }
}