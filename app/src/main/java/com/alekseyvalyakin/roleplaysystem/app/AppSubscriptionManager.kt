package com.alekseyvalyakin.roleplaysystem.app

import com.alekseyvalyakin.roleplaysystem.base.observer.DisposableObserver
import com.alekseyvalyakin.roleplaysystem.data.sound.SoundObserver

class AppSubscriptionManager(
        private val recordSoundObserver: DisposableObserver,
        private val soundPlayObserver: DisposableObserver,
        private val soundObserver: SoundObserver
) {
    fun subscribe() {
        recordSoundObserver.subscribe()
        soundPlayObserver.subscribe()
        soundObserver.subscribe()
    }
}