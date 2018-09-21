package com.alekseyvalyakin.roleplaysystem.utils

import android.Manifest
import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasId
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import timber.log.Timber

fun <T> Observable<T>.subscribeWithErrorLogging(onNext: (T) -> Unit): Disposable {
    return this.subscribe(onNext) {
        Timber.e(it)
    }
}

fun <T> Observable<T>.subscribeWithErrorLogging(): Disposable {
    return this.subscribeWithErrorLogging {}
}

fun Completable.subscribeWithErrorLogging(onComplete: () -> Unit): Disposable {
    return this.subscribe(onComplete) {
        Timber.e(it)
    }
}

fun Completable.subscribeWithErrorLogging(): Disposable {
    return this.subscribeWithErrorLogging {}
}

fun <T> Single<T>.subscribeWithErrorLogging(onNext: (T) -> Unit): Disposable {
    return this.subscribe(onNext) {
        Timber.e(it)
    }
}

fun <T> Single<T>.subscribeWithErrorLogging(): Disposable {
    return this.subscribeWithErrorLogging {}
}

fun <T> Flowable<T>.subscribeWithErrorLogging(onNext: (T) -> Unit): Disposable {
    return this.subscribe(onNext) {
        Timber.e(it)
    }
}

fun <T> Flowable<T>.subscribeWithErrorLogging(): Disposable {
    return this.subscribeWithErrorLogging {}
}

fun <T : HasId> Flowable<T>.setId(id: String): Flowable<T> {
    return this.doOnNext { it.id = id }
}

fun <T : HasId> Single<T>.setId(id: String): Single<T> {
    return this.doOnSuccess { it.id = id }
}

fun <T : HasId> Maybe<T>.setId(id: String): Maybe<T> {
    return this.doOnSuccess { it.id = id }
}

fun <T> createSingle(body: () -> T, scheduler: Scheduler): Single<T> {
    return Single.fromCallable {
        body()
    }.subscribeOn(scheduler)
}

fun <T> createCompletable(body: () -> T, scheduler: Scheduler): Completable {
    return Completable.fromCallable {
        body()
    }.subscribeOn(scheduler)
}

fun <T> Observable<T>.requestPermissions(rxPermissions: RxPermissions, vararg permissions: String): Observable<T> {
    return this.observeOn(AndroidSchedulers.mainThread())
            .concatMap { any ->
                rxPermissions.request(*permissions)
                        .filter { it }
                        .map { any }
            }
}

fun <T> Observable<T>.requestPermissionsExternalReadWrite(rxPermissions: RxPermissions): Observable<T> {
    return requestPermissions(rxPermissions,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
}