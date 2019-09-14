package com.rxfirebase2

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import io.reactivex.Completable
import io.reactivex.Single


fun AppUpdateManager.getAppUpdateInfoSingle(): Single<AppUpdateInfo> {
    return Single.create { emitter ->
        appUpdateInfo.addOnSuccessListener {
            emitter.onSuccess(it)
        }.addOnFailureListener {
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


