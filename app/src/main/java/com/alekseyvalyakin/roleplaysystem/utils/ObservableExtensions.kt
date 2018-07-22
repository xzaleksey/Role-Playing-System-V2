package com.alekseyvalyakin.roleplaysystem.utils

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import timber.log.Timber

fun <T> Observable<T>.subscribeWithErrorLogging(onNext: (T) -> Unit): Disposable {
    return this.subscribe(onNext, {
        Timber.e(it)
    })
}