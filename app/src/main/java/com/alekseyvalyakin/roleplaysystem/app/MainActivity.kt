package com.alekseyvalyakin.roleplaysystem.app

import android.os.Bundle
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityModule
import com.alekseyvalyakin.roleplaysystem.ribs.root.RootBuilder
import com.alekseyvalyakin.roleplaysystem.utils.reporter.AnalyticsReporter
import com.uber.rib.core.RibActivity
import com.uber.rib.core.ViewRouter
import javax.inject.Inject

class MainActivity : RibActivity() {

    @Inject
    lateinit var analyticsReporter: AnalyticsReporter

    override fun createRouter(parentViewGroup: ViewGroup): ViewRouter<*, *, *> {
        val component = RpsApp.app.getAppComponent()
                .activityComponent(ActivityModule(this))
        component.inject(this)
        return RootBuilder(component).build(parentViewGroup)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analyticsReporter.attachActivity(this)
    }

    override fun onDestroy() {
        analyticsReporter.detachActivity()
        super.onDestroy()
    }
}
