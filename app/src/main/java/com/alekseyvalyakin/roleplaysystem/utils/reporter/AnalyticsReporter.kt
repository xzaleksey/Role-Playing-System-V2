package com.alekseyvalyakin.roleplaysystem.utils.reporter

import android.app.Activity
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.uber.rib.core.toAndroidBundle
import java.lang.ref.WeakReference

class AnalyticsReporterImpl(context: Context) : AnalyticsReporter {

    private val fireBaseAnalytics = FirebaseAnalytics.getInstance(context)
    private var activity = WeakReference<Activity>(null)

    override fun setCurrentUser(userId: String?) {
        fireBaseAnalytics.setUserId(userId)
    }

    override fun logEvent(event: AnalyticsEvent) {
        fireBaseAnalytics.logEvent(event.name,
                event.bundle.toAndroidBundle())
    }

    override fun setUserEmail(email: String) {
        setUserProperty(EMAIL_KEY, email)
    }

    override fun reset() {
        return fireBaseAnalytics.resetAnalyticsData()
    }

    override fun setCurrentScreen(currentScreenName: String, currentScreenClass: String) {
        activity.get()?.run {
            fireBaseAnalytics.setCurrentScreen(this, currentScreenName, currentScreenClass)
        }
    }

    override fun attachActivity(activity: Activity) {
        this.activity = WeakReference(activity)
    }

    override fun detachActivity() {
        this.activity = WeakReference<Activity>(null)
    }

    private fun setUserProperty(key: String, value: String) {
        fireBaseAnalytics.setUserProperty(key, value)
    }

    companion object {
        const val EMAIL_KEY = "email"
    }
}

interface AnalyticsReporter {
    fun setCurrentUser(userId: String?)

    fun logEvent(event: AnalyticsEvent)

    fun setUserEmail(email: String)

    fun reset()

    fun attachActivity(activity: Activity)

    fun detachActivity()

    fun setCurrentScreen(currentScreenName: String, currentScreenClass: String)
}

data class AnalyticsEvent(
        val name: String,
        val bundle: com.uber.rib.core.Bundle
)