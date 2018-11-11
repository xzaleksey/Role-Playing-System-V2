package com.alekseyvalyakin.roleplaysystem.base.observer

import io.reactivex.disposables.Disposable

interface DisposableObserver {
    fun subscribe(): Disposable
}