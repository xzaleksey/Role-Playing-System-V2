package com.alekseyvalyakin.roleplaysystem.utils.reporter

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.uber.rib.core.toAndroidBundle

class AnalyticsReporterImpl(context: Context) : AnalyticsReporter {
    private val fireBaseAnalytics = FirebaseAnalytics.getInstance(context)

    override fun setCurrentUser(userId: String?) {
        fireBaseAnalytics.setUserId(userId)
    }

    override fun logEvent(event: AnalyticsEvent) {
        fireBaseAnalytics.logEvent(event.name,
                event.bundle.toAndroidBundle())
    }

    override fun setUserProperty(userProperty: UserProperty) {
        fireBaseAnalytics.setUserProperty(userProperty.name, userProperty.value)
    }
}

interface AnalyticsReporter {
    fun setCurrentUser(userId: String?)

    fun logEvent(event: AnalyticsEvent)

    fun setUserProperty(userProperty: UserProperty)
}

data class AnalyticsEvent(
        val name: String,
        val bundle: com.uber.rib.core.Bundle
)

data class UserProperty(
        val name: String,
        val value: String
)