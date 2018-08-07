package com.alekseyvalyakin.roleplaysystem.utils

import com.alekseyvalyakin.roleplaysystem.data.firestore.core.HasId
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
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