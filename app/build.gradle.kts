import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone.getDefault

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.about.library)
    alias(libs.plugins.ksp)
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization") version "2.1.20"
}

@Suppress("UnstableApiUsage")
android {
    namespace = "com.smart.htu"

    compileSdk {
        version = release(37)
    }

    val keystorePath = System.getenv("KEYSTORE_PATH")
    if (keystorePath != null) {
        signingConfigs {
            create("release") {
                storeFile = rootProject.file(keystorePath)
                storePassword = System.getenv("STORE_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD")
            }
        }
    }

    defaultConfig {
        applicationId = "com.smart.htu"
        minSdk = 33
        targetSdk = 37
        versionCode = 202607072
        versionName = "3.2.0"

        val buildTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").apply {
            timeZone = getDefault()
        }.format(Date())

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "BUILD_TIME", "\"$buildTime\"")
    }

    experimentalProperties["android.experimental.r8.dex-startup-optimization"] = true
    buildTypes {
        release {
            optimization.enable = true
            vcsInfo.include = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (keystorePath != null) signingConfig = signingConfigs.getByName("release")
        }

        debug {
            if (keystorePath != null) signingConfig = signingConfigs.getByName("release")
        }
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1,DEPENDENCIES}"
            excludes += "/META-INF/versions/9/OSGI-INF/MANIFEST.MF"
        }
    }
}

base {
    archivesName.set(
        "SmartHNU-v${android.defaultConfig.versionName}(${android.defaultConfig.versionCode})",
    )
}

aboutLibraries {
    export {
        outputFile = file("res/raw/aboutlibraries.json")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

    implementation(libs.androidx.navigation3.runtime)

    ksp(libs.kotlin.metadata.jvm)

    // miuix
    implementation(libs.miuix.ui)
    implementation(libs.miuix.preference)
    implementation(libs.miuix.icons)
    implementation(libs.miuix.blur)
    implementation(libs.miuix.navigation3.ui)
    implementation(libs.miuix.squircle)
    implementation(libs.miuix.shader)

    implementation(libs.material.kolor)

    // Splash
    implementation(libs.androidx.core.splashscreen)

    // dataStore
    implementation(libs.androidx.datastore)

    // Coil
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.zoomable.image.coil3)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Confetti
    implementation(libs.konfetti)

    // webview
    implementation(libs.androidx.webkit)
    implementation(libs.webview)

    // json
    implementation(libs.kotlinx.serialization.json)

    // Jsoup
    implementation(libs.jsoup)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.scalars)
    implementation(libs.converter.gson)

    // Coroutine 支持
    implementation(libs.kotlinx.coroutines.android)

    // About Screen
    implementation(libs.about.library)

    // chart
    implementation(libs.vico)

    // JWT
    implementation(libs.eddsa)

    implementation(libs.kotlinx.datetime)

    implementation(libs.readability4j)

    // 为 HyperOS 提供焦点通知 需要解除白名单限制，A16 以上系统可以选择使用原生 LiveData
    implementation("com.xzakota.hyper.notification:focus-api:1.4")

}