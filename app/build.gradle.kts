plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.aboutlibraries)
    kotlin("plugin.serialization") version "2.0.0"
}

android {
    namespace = "com.smart.htu"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.smart.htu"
        minSdk = 29
        targetSdk = 33
        versionCode = 202502231
        versionName = "3.0.9.7-beta"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
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
    implementation(libs.androidx.compose.material.material.icons.core)
    // implementation(libs.androidx.material.icons.extended)
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

    implementation(libs.androidx.material3.adaptive.navigation.suite)
    implementation(libs.androidx.adaptive.navigation.android)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.ui.tiles)

    // Splash
    implementation(libs.androidx.core.splashscreen)

    // dataStore
    implementation(libs.androidx.datastore.preferences)

    // Crypt
    implementation(libs.androidx.security.crypto)

    // Coil
    implementation(libs.coil.base)
    implementation(libs.coil.compose)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // 阴历
    implementation(libs.xtail.lunar)

    // Konfetti
    implementation(libs.dionsegijn.konfetti.compose)

    // haze
    implementation(libs.haze)

    // webview
    implementation(libs.androidx.webkit)
    implementation(libs.compose.webview.multiplatform)

    // json
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.jakewharton.retrofit2.kotlinx.serialization.converter)
    implementation(libs.converter.gson)

    // Jsoup
    implementation(libs.jsoup)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.scalars)

    // Coroutine 支持
    implementation(libs.kotlinx.coroutines.android)

    // About Screen
    implementation(libs.aboutlibraries.core)
    implementation(libs.aboutlibraries.compose.m3)

    // vico
    implementation(libs.vico.compose.m3)
}