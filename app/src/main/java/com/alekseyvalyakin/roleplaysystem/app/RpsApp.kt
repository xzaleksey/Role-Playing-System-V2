package com.alekseyvalyakin.roleplaysystem.app

import android.app.Application
import android.support.v7.app.AppCompatDelegate
import com.alekseyvalyakin.roleplaysystem.BuildConfig
import com.alekseyvalyakin.roleplaysystem.di.singleton.AppComponent
import com.alekseyvalyakin.roleplaysystem.di.singleton.AppModule
import com.alekseyvalyakin.roleplaysystem.di.singleton.DaggerAppComponent
import com.crashlytics.android.Crashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
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
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        FirebaseFirestore.getInstance().firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setTimestampsInSnapshotsEnabled(true)
                .build()

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