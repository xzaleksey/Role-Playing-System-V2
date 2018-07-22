package com.alekseyvalyakin.roleplaysystem.app

import android.os.Bundle
import android.view.ViewGroup
import com.alekseyvalyakin.roleplaysystem.di.activity.ActivityModule
import com.alekseyvalyakin.roleplaysystem.ribs.root.RootBuilder
import com.alekseyvalyakin.roleplaysystem.utils.KeyboardUtil
import com.uber.rib.core.RibActivity
import com.uber.rib.core.ViewRouter

class MainActivity : RibActivity() {

    private lateinit var softInputAssist: KeyboardUtil

    override fun createRouter(parentViewGroup: ViewGroup): ViewRouter<*, *, *> {
        return RootBuilder(RpsApp.app.getAppComponent()
                .activityComponent(ActivityModule(this)))
                .build(parentViewGroup)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        softInputAssist = KeyboardUtil(this)
    }

    override fun onPause() {
        softInputAssist.disable()
        super.onPause()
    }

    override fun onResume() {
        softInputAssist.enable()
        super.onResume()
    }

    override fun onDestroy() {
//        softInputAssist.onDestroy()
        super.onDestroy()
    }
}
