package com.alekseyvalyakin.roleplaysystem.utils.keyboard

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST
import com.alekseyvalyakin.roleplaysystem.utils.dip
import com.alekseyvalyakin.roleplaysystem.utils.subscribeWithErrorLogging
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import org.jetbrains.anko.contentView
import java.util.concurrent.TimeUnit

class KeyboardStateProviderImpl(private val activity: Activity) : KeyboardStateProvider {
    private val rootViewRect = Rect()
    private val visibleThreshold: Int = KEYBOARD_VISIBLE_THRESHOLD_PX

    private val isSoftInputMethodSupported: Boolean
        get() {
            val softInputMethod = activity.window.attributes.softInputMode and SOFT_INPUT_MASK_ADJUST
            return softInputMethod == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE || softInputMethod == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED
        }

    private val observable by lazy {
        Observable.create<Boolean>(ObservableOnSubscribe { emitter ->
            if (!isSoftInputMethodSupported) {
                emitter.onError(IllegalStateException("Activity window SoftInputMethod must be ADJUST_RESIZE"))
                return@ObservableOnSubscribe
            }
            val activityRoot = getActivityRoot(activity)
            if (activityRoot == null) {
                Observable.timer(50L, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWithErrorLogging {
                            if (!activity.isFinishing) {
                                start(getActivityRoot(activity)!!, emitter)
                            } else {
                                emitter.onComplete()
                            }
                        }
                return@ObservableOnSubscribe
            } else {
                start(activityRoot, emitter)
            }
        }).share()
    }

    private fun start(activityRoot: View, emitter: ObservableEmitter<Boolean>) {
        val keyboardLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {

            private var wasShown = false

            override fun onGlobalLayout() {
                val isShown = isKeyboardShown(activityRoot)
                if (isShown == wasShown) {
                    return
                }
                wasShown = isShown
                emitter.onNext(isShown)
            }
        }

        emitter.setCancellable { activityRoot.viewTreeObserver.removeOnGlobalLayoutListener(keyboardLayoutListener) }
        activityRoot.viewTreeObserver.addOnGlobalLayoutListener(keyboardLayoutListener)
    }

    override fun observeKeyboardState(): Observable<Boolean> {
        return observable
    }

    private fun getActivityRoot(activity: Activity): View? {
        return activity.contentView
    }

    private fun isKeyboardShown(activityRoot: View): Boolean {
        activityRoot.getWindowVisibleDisplayFrame(rootViewRect)

        val heightDiff = activityRoot.rootView.height - rootViewRect.height()

        return heightDiff > visibleThreshold
    }

    companion object {
        private val KEYBOARD_VISIBLE_THRESHOLD_PX = 100.dip()
    }
}