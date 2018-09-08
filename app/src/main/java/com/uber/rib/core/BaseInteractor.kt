package com.uber.rib.core

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import java.io.Serializable

@Suppress("FINITE_BOUNDS_VIOLATION_IN_JAVA")
abstract class BaseInteractor<P, R : Router<out Interactor<*, *>, out InteractorBaseComponent<*>>> : Interactor<P, R>() {
    private val compositeDisposable = CompositeDisposable()
    private val childrenKey = "childrenKey"
    protected val modelKey = "modelKey"

    fun addDisposable(disposable: Disposable) {
        disposable.addTo(compositeDisposable)
    }

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        savedInstanceState?.getSerializable<ArrayList<Pair<Class<Router<out Interactor<*, *>, out InteractorBaseComponent<*>>>, Serializable?>>>(childrenKey)
                ?.forEach { pair ->
                    restoreRouter(pair.first, pair.second)
                }
        Timber.d("router children size %s", router.children.size)
    }

    open fun <T> restoreRouter(clazz: Class<T>, childInfo: Serializable?) where T : Router<out Interactor<*, *>, out InteractorBaseComponent<*>> {

    }

    override fun onSaveInstanceState(outState: Bundle) {
        val pairs = router.children.map { r ->
            if (r is RestorableRouter) {
                return@map r.javaClass to r.getRestorableInfo()
            }
            return@map r.javaClass to null
        }
        outState.putSerializable(childrenKey, ArrayList(pairs))
    }

    override fun willResignActive() {
        super.willResignActive()
        compositeDisposable.clear()
    }

    protected fun Disposable.addToDisposables() {
        compositeDisposable.add(this)
    }
}