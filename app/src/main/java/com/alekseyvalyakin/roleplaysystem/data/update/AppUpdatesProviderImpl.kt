package com.alekseyvalyakin.roleplaysystem.data.update

import android.app.Activity
import com.alekseyvalyakin.roleplaysystem.app.MainActivity
import com.alekseyvalyakin.roleplaysystem.utils.RequestCodes.RC_APP_UPDATE
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.jakewharton.rxrelay2.PublishRelay
import com.rxfirebase2.completeUpdateCompletable
import com.rxfirebase2.getAppUpdateInfoSingle
import com.uber.rib.core.lifecycle.ActivityCallbackEvent
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import timber.log.Timber

class AppUpdatesProviderImpl constructor(
        private val activity: MainActivity
) : AppUpdatesProvider {

    private val relay = PublishRelay.create<AppUpdatesProvider.Event>()

    private val appUpdateManager = AppUpdateManagerFactory.create(activity)
//    private val appUpdateManager = FakeAppUpdateManager(activity).apply {
//        setUpdateAvailable(UpdateAvailability.UPDATE_AVAILABLE)
//    }

    private val activityResultObservable = activity.callbacks()
            .filter { it.type == ActivityCallbackEvent.Type.ACTIVITY_RESULT }
            .cast(ActivityCallbackEvent.ActivityResult::class.java)
            .filter { it.requestCode == RC_APP_UPDATE }
            .take(1)

    private val installStateListener = InstallStateUpdatedListener { state ->
        Timber.d("appUpdate: got $state")
        when (state.installStatus()) {
            InstallStatus.FAILED -> {
                Timber.e("appUpdate: failed to install update")
                relay.accept(AppUpdatesProvider.Event.DownloadFailed)
            }
            InstallStatus.CANCELED -> {
                Timber.e("appUpdate: user cancelled update")
                relay.accept(AppUpdatesProvider.Event.UserCanceledDownload)
            }
            InstallStatus.DOWNLOADED -> {
                Timber.e("appUpdate: downloaded update")
                relay.accept(AppUpdatesProvider.Event.DownloadFinished)
            }
        }
    }

    private val observable = relay
            .doOnSubscribe {
                Timber.d("appUpdate: register listener")
                appUpdateManager.registerListener(installStateListener)
            }
            .doOnDispose {
                Timber.d("appUpdate: unregister listener")
                appUpdateManager.unregisterListener(installStateListener)
            }
            .share()

    private fun observeEvents(): Observable<AppUpdatesProvider.Event> {
        return observable
    }

    override fun isAppUpdateAvailable(): Single<Boolean> {
        return appUpdateManager.getAppUpdateInfoSingle().map {
            isFlexibleUpdateAvailable(it)
        }
    }

    override fun triggerFlexibleUpdate(): Observable<AppUpdatesProvider.Event> {
        Timber.d("appUpdate: subscribe updates")
        return observeEvents()
                .startWith { observer ->
                    appUpdateManager.getAppUpdateInfoSingle()
                            .doAfterSuccess { appUpdateInfo ->
                                if (isFlexibleUpdateAvailable(appUpdateInfo)) {
                                    Timber.d("appUpdate: before starting flexible update")
                                    handleUserInteraction(observer)
//                                    appUpdateManager.userAcceptsUpdate()
                                    appUpdateManager.startUpdateFlowForResult(
                                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                            appUpdateInfo,
                                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                                            AppUpdateType.FLEXIBLE,
                                            // The current activity making the update request.
                                            activity,
                                            // Include a request code to later monitor this update request.
                                            RC_APP_UPDATE)
                                } else {
                                    Timber.d("appUpdate: error state")
                                    observer.onError(RuntimeException("No flexible update available"))
                                }
//                                observer.onNext(AppUpdatesProvider.Event.StartedDownloading)
//                                observer.onComplete()
//                                appUpdateManager.userAcceptsUpdate()
//                                appUpdateManager.downloadStarts()
//                                appUpdateManager.downloadCompletes()
                            }.doOnError { observer.onError(it) }
                            .subscribeWithErrorLogging()
                }

    }

    private fun handleUserInteraction(observer: Observer<in AppUpdatesProvider.Event>) {
        activityResultObservable.subscribeWithErrorLogging {
            if (it.resultCode == Activity.RESULT_OK) {
                observer.onNext(AppUpdatesProvider.Event.StartedDownloading)
                observer.onComplete()
            } else {
                observer.onNext(AppUpdatesProvider.Event.UserCanceledDownload)
                observer.onComplete()
            }
        }
    }

    override fun completeUpdate(): Completable {
        return appUpdateManager.completeUpdateCompletable()
    }

    private fun isFlexibleUpdateAvailable(it: AppUpdateInfo) =
            (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && it.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE))

}