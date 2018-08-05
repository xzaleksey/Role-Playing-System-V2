package com.alekseyvalyakin.roleplaysystem.di.activity

import com.alekseyvalyakin.roleplaysystem.app.MainActivity

class ActivityListenerImpl(
        private val mainActivity: MainActivity
) : ActivityListener {

    override fun backPress() {
        mainActivity.onBackPressed()
    }
}