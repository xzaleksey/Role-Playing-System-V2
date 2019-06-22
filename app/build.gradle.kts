import Versions.anko_version
import Versions.autodispose_version
import Versions.room_version

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("io.fabric")
    id("kotlin-kapt")
}

apply {
    plugin("kotlin-android")
    plugin("kotlin-android-extensions")
}

apply(from = "master_password.gradle")

//val keystorePropertiesFile = rootProject.file("keystore.properties")
//val keystoreProperties = java.util.Properties()
//keystoreProperties.load(java.io.FileInputStream(keystorePropertiesFile))


android {
    compileSdkVersion(28)
    buildToolsVersion("28.0.3")
    defaultConfig {
        applicationId = "com.alekseyvalyakin.roleplaysystem"
        minSdkVersion(21)
        targetSdkVersion(28)
        versionCode = 12
        versionName = "1.1.2"
        buildConfigField("String", "MASTER_PASSWORD", "\"$project.password\"")
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments.put("room.schemaLocation", "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        get("release").apply {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFile(getDefaultProguardFile("proguard-android.txt"))
            proguardFile(file("proguard-rules.pro"))
//            signingConfig = signingConfigs.getByName("release")
        }
        get("debug").apply {
            ext["alwaysUpdateBuildId"] = false
        }
    }

    signingConfigs {
        //        getByName("release") {
//            keyAlias = keystoreProperties["keyAlias"]
//            keyPassword = keystoreProperties["keyPassword"]
//            storeFile = file(keystoreProperties["storeFile"])
//            storePassword = keystoreProperties["storePassword"]
//        }
    }


    compileOptions {
        targetCompatibility = JavaVersion.VERSION_1_8
        setSourceCompatibility(JavaVersion.VERSION_1_8)
    }

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
    implementation(Libs.support_annotations)
    implementation(Libs.support_appcompat_v7)
    implementation(Libs.support_design)
    implementation("androidx.cardview:cardview:${Versions.support_version}")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation(Libs.firebase_core)
    implementation(Libs.kotlin_std)
    implementation(Libs.crashlytics)
    implementation(Libs.firebase_auth)
    implementation(Libs.firebase_storage)
    implementation(Libs.firebase_store)
    implementation(Libs.firebase_messaging)
    implementation(Libs.firebase_config)
//    implementation(Libs.firebase_ml)
    implementation(Libs.firebase_functions)
    implementation("com.google.android.gms:play-services-auth:16.0.1")
    implementation(Libs.rx_java2)
    implementation(Libs.rx_android)
    implementation("io.reactivex.rxjava2:rxkotlin:2.3.0")
    implementation("com.google.dagger:dagger:2.23")
    kapt("com.google.dagger:dagger-android-processor:2.22.1")
    kapt("com.google.dagger:dagger-compiler:2.22.1")
    implementation("com.jakewharton.rxbinding2:rxbinding:2.1.1")
    implementation("com.jakewharton.rxbinding2:rxbinding-design:2.1.1")
    implementation("com.jakewharton.rxbinding2:rxbinding-recyclerview-v7:2.1.1")
    implementation("com.jakewharton.timber:timber:4.7.1")
    implementation("net.danlew:android.joda:2.10.1.1")

    implementation("android.arch.work:work-runtime:1.0.1")
//// if you want some benefits from Kotlin then try this
    implementation ("android.arch.work:work-runtime-ktx:1.0.1")

//    //Room
    implementation ("android.arch.persistence.room:runtime:$room_version")
    kapt ("android.arch.persistence.room:compiler:$room_version")
    testImplementation ("android.arch.persistence.room:testing:$room_version")
    implementation ("android.arch.persistence.room:rxjava2:$room_version")
//
//    // sdk15, sdk19, sdk21, sdk23 are also available
    implementation ("org.jetbrains.anko:anko-commons:$anko_version")
    implementation ("org.jetbrains.anko:anko-sdk25:$anko_version")
    implementation ("org.jetbrains.anko:anko-appcompat-v7:$anko_version")
    implementation ("org.jetbrains.anko:anko-cardview-v7:$anko_version")
    implementation ("org.jetbrains.anko:anko-design:$anko_version")
    implementation ("org.jetbrains.anko:anko-recyclerview-v7:$anko_version")
    implementation ("org.jetbrains.anko:anko-constraint-layout:$anko_version")

    implementation ("org.jetbrains.anko:anko-appcompat-v7-coroutines:$anko_version")
    implementation ("org.jetbrains.anko:anko-sdk25-coroutines:$anko_version")
    implementation ("org.jetbrains.anko:anko-design-coroutines:$anko_version")
    implementation ("com.google.android:flexbox:1.0.0")

    implementation ("com.nineoldandroids:library:2.4.0")
    implementation ("com.github.bumptech.glide:glide:4.7.1")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.7.1")
    implementation ("id.zelory:compressor:2.1.0")
    implementation ("com.github.chrisbanes:PhotoView:2.1.4")


    implementation("com.kbeanie:multipicker:1.5@aar")
    implementation ("com.afollestad.material-dialogs:core:2.0.0-alpha07")
    implementation ("com.afollestad.material-dialogs:input:2.0.0-alpha07")
    implementation ("com.github.tbruyelle:rxpermissions:0.10.2")
    implementation( "com.jakewharton.rxrelay2:rxrelay:2.1.0")
    implementation( "eu.davidea:flexible-adapter:5.0.0-rc1")
    implementation ("com.daimajia.easing:library:2.0@aar")
    implementation ("com.daimajia.androidanimations:library:2.3@aar")

    implementation ("com.google.android.exoplayer:exoplayer:2.9.0")
    implementation ("com.google.android.exoplayer:exoplayer-ui:2.9.0")

    kapt ("com.uber.rib:rib-compiler-test:0.9.1")

    implementation ("com.uber.autodispose:autodispose-android:$autodispose_version@aar")
    testImplementation ("com.uber.rib:rib-test-utils:0.9.1")

    testImplementation ("junit:junit:4.12")
    androidTestImplementation ("com.android.support.test:runner:1.0.2")
    androidTestImplementation ("com.android.support.test.espresso:espresso-core:3.0.2")
}

configurations {
    all {
        exclude("io.reactivex:rxjava", "rxjava")
    }
}
repositories {
    mavenLocal()
    mavenCentral()
    jcenter()
}

apply {
    plugin("com.google.gms.google-services")
}
