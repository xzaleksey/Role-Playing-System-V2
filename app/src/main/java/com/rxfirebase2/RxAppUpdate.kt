package com.rxfirebase2

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import io.reactivex.Completable
import io.reactivex.Single
import timber.log.Timber


fun AppUpdateManager.getAppUpdateInfoSingle(): Single<AppUpdateInfo> {
    return Single.create { emitter ->
        Timber.d("appUpdate: request updateInfo")
        appUpdateInfo.addOnSuccessListener {
            Timber.d("appUpdate: got update info $it")
            emitter.onSuccess(it)
        }.addOnFailureListener {
            Timber.d("appUpdate: got update error $it")
            emitter.onError(it)
        }
    }
}

fun AppUpdateManager.completeUpdateCompletable(): Completable {
    return Completable.create { emitter ->
        completeUpdate().addOnSuccessListener {
            emitter.onComplete()
        }.addOnFailureListener { emitter.onError(it) }
    }
}


