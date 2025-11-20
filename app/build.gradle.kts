import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone.getDefault

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.about.library)
    alias(libs.plugins.ksp)
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization") version "2.1.20"
}

android {
    namespace = "com.smart.htu"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.smart.htu"
        minSdk = 29
        targetSdk = 35
        versionCode = 202511201
        versionName = "3.1.0"

        val buildTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").apply {
            timeZone = getDefault()
        }.format(Date())

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "BUILD_TIME", "\"$buildTime\"")
    }

    aboutLibraries {
        export {
            outputFile = file("res/raw/aboutlibraries.json")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/versions/9/OSGI-INF/MANIFEST.MF"
        }
    }

    android.applicationVariants.configureEach {
        val variant = this
        outputs.configureEach {
            if (this is BaseVariantOutputImpl) {
                outputFileName =
                    "SmartHNU_v${variant.versionName}(${variant.versionCode})_${variant.buildType.name}.apk"
            }
        }
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
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.core)

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
    implementation(libs.androidx.navigation3.ui)

    implementation(libs.androidx.adaptive)
    implementation(libs.androidx.adaptive.layout)
    implementation(libs.androidx.adaptive.navigation)
    // miuix
    implementation(libs.miuix)
    implementation(libs.capsule)

    // Splash
    implementation(libs.androidx.core.splashscreen)

    // dataStore
    implementation(libs.androidx.datastore.preferences)

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

    // haze
    implementation(libs.haze)

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
    implementation(libs.aboutlibraries.compose.m3)

    // chart
    implementation(libs.vico)

    // JWT
    implementation(libs.eddsa)

    // pdf
    implementation(libs.pdf.viewer)

    // reorderable
    implementation(libs.reorderable)

    implementation(libs.datetime.wheel.picker)
    implementation(libs.kotlinx.datetime)

    implementation(libs.compose.markdown)
}