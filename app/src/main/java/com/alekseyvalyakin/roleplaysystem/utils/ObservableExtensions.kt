package com.alekseyvalyakin.roleplaysystem.utils

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import timber.log.Timber

fun <T> Observable<T>.subscribeWithErrorLogging(onNext: (T) -> Unit): Disposable {
    return this.subscribe(onNext, {
        Timber.e(it)
    })
}

fun <T> Observable<T>.subscribeWithErrorLogging(): Disposable {
    return this.subscribeWithErrorLogging {}
}

fun <T> Flowable<T>.subscribeWithErrorLogging(onNext: (T) -> Unit): Disposable {
    return this.subscribe(onNext, {
        Timber.e(it)
    })
}

fun <T> Flowable<T>.subscribeWithErrorLogging(): Disposable {
    return this.subscribeWithErrorLogging {}
}