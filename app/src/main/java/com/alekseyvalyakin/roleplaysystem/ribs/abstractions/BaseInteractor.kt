package com.alekseyvalyakin.roleplaysystem.ribs.abstractions

import com.uber.rib.core.Interactor
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.Router
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")
open class BaseInteractor<P, R : Router<out Interactor<*, *>, out InteractorBaseComponent<*>>> : Interactor<P, R>() {
    private val compositeDisposable = CompositeDisposable()

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun willResignActive() {
        super.willResignActive()
        compositeDisposable.clear()
    }
}