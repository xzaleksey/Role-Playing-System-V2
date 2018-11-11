package com.alekseyvalyakin.roleplaysystem.app

import android.app.Application
import com.alekseyvalyakin.roleplaysystem.BuildConfig
import com.alekseyvalyakin.roleplaysystem.R
import com.alekseyvalyakin.roleplaysystem.di.singleton.AppComponent
import com.alekseyvalyakin.roleplaysystem.di.singleton.AppModule
import com.alekseyvalyakin.roleplaysystem.di.singleton.DaggerAppComponent
import com.alekseyvalyakin.roleplaysystem.tree.MyTree
import com.crashlytics.android.Crashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import io.fabric.sdk.android.Fabric
import net.danlew.android.joda.JodaTimeAndroid
import timber.log.Timber


class RpsApp : Application() {
    private var component: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        app = this
        Fabric.with(this, Crashlytics())
        JodaTimeAndroid.init(this)
        Timber.plant(MyTree())
        FirebaseFirestore.getInstance().firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setTimestampsInSnapshotsEnabled(true)
                .build()

        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        remoteConfig.setConfigSettings(configSettings)
        remoteConfig.setDefaults(R.xml.default_config)

        getAppComponent().remoteConfigProvider().fetch()
        getAppComponent().appSubscriptionManager().subscribe()
    }

    fun getAppComponent(): AppComponent {
        if (app.component == null) {
            app.component = DaggerAppComponent.builder()
                    .appModule(getApplicationModule())
                    .build()
        }

        return app.component!!
    }

    private fun getApplicationModule(): AppModule {
        return AppModule(this)
    }

    companion object {
        lateinit var app: RpsApp
    }
}