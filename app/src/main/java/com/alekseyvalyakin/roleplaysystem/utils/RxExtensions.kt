package com.alekseyvalyakin.roleplaysystem.utils

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