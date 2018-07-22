package com.alekseyvalyakin.roleplaysystem.app

import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityModule
import com.alekseyvalyakin.roleplaysystem.ribs.root.RootBuilder
import com.uber.rib.core.RibActivity
import com.uber.rib.core.ViewRouter

class MainActivity : RibActivity() {

    override fun createRouter(parentViewGroup: ViewGroup): ViewRouter<*, *, *> {
        return RootBuilder(RpsApp.app.getAppComponent()
                .activityComponent(ActivityModule(this)))
                .build(parentViewGroup)
    }
}
