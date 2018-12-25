package com.uber.rib.core

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo

@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")
abstract class BaseInteractor<P, R : Router<out Interactor<*, *>, out InteractorBaseComponent<*>>> : Interactor<P, R>() {
    private val compositeDisposable = CompositeDisposable()
    private val childrenKey = "childrenKey"
    protected val modelKey = "modelKey"

    fun addDisposable(disposable: Disposable) {
        disposable.addTo(compositeDisposable)
    }

    override fun willResignActive() {
        super.willResignActive()
        compositeDisposable.clear()
    }

    protected fun Disposable.addToDisposables() {
        compositeDisposable.add(this)
    }
}