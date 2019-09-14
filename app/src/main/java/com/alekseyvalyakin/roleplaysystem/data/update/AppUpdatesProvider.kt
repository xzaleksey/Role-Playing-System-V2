package com.alekseyvalyakin.roleplaysystem.data.update

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface AppUpdatesProvider {
    fun triggerFlexibleUpdate(): Observable<Event>

    fun isAppUpdateAvailable(): Single<Boolean>

    fun completeUpdate(): Completable

    sealed class Event {
        object DownloadFinished : Event()
        object UserCanceledDownload : Event()
        object DownloadFailed : Event()
        object StartedDownloading : Event()
    }
}