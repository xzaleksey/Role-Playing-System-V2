package com.uber.rib.core

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber

@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")
abstract class BaseInteractor<P, R : Router<out Interactor<*, *>, out InteractorBaseComponent<*>>> : Interactor<P, R>() {
    private val compositeDisposable = CompositeDisposable()
    private val childrenKey = "childrenKey"

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        savedInstanceState?.getSerializable<ArrayList<Class<Router<out Interactor<*, *>, out InteractorBaseComponent<*>>>>>(childrenKey)?.forEach {
            restoreRouter(it)
        }
        Timber.d("router children size " + router.children.size)
    }

    open fun <T> restoreRouter(clazz: Class<T>) where T : Router<out Interactor<*, *>, out InteractorBaseComponent<*>> {

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(childrenKey, ArrayList(router.children.map { r -> r.javaClass }))
    }

    override fun willResignActive() {
        super.willResignActive()
        compositeDisposable.clear()
    }

    protected fun Disposable.addToDisposables() {
        compositeDisposable.add(this)
    }
}