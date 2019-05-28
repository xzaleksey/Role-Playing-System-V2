package com.alekseyvalyakin.roleplaysystem.utils

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.ObservableEmitter
import io.reactivex.SingleEmitter


fun <T> SingleEmitter<T>.throwError(t: Throwable) {
    if (!this.isDisposed) {
        this.onError(t)
    }
}

fun <T> ObservableEmitter<T>.throwError(t: Throwable) {
    if (!this.isDisposed) {
        this.onError(t)
    }
}

fun <T> BehaviorRelay<T>.getNonNullValue():T {
    return this.value!!
}