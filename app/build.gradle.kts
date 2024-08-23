plugins {
    id("com.android.application")
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.xhand.hnu"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.xhand.hnu"
        minSdk = 29
        targetSdk = 33
        versionCode = 1
        versionName = "2.2.5"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4")
    implementation("androidx.activity:activity-compose:1.9.1")
    implementation(platform("androidx.compose:compose-bom:2024.08.00"))
    implementation("androidx.compose.ui:ui:1.6.8")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.3.0-rc01")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.3.0-rc01")
    implementation("androidx.compose.material3.adaptive:adaptive-navigation-android:1.0.0-rc01")

    val accompanistVersion = "0.34.0"
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-placeholder-material:0.35.1-alpha")

    // html解析
    implementation("org.jsoup:jsoup:1.18.1")
    implementation("com.chimbori.crux:crux:5.0.0")
    implementation("net.dankito.readability4j:readability4j:1.0.8")

    // Network
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.github.franmontiel:PersistentCookieJar:1.0.1")

    // datastore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Chart
    val chartVersion = "2.0.0-alpha.27"
    implementation("com.patrykandpatrick.vico:compose:$chartVersion")
    implementation("com.patrykandpatrick.vico:compose-m3:$chartVersion")
    implementation("com.patrykandpatrick.vico:core:$chartVersion")
    implementation("com.patrykandpatrick.vico:views:$chartVersion")

    // 加载picture
    implementation("io.coil-kt:coil-compose:2.6.0")

    // datastore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    implementation("com.github.jeziellago:compose-markdown:0.5.3")

    implementation("org.jetbrains.kotlin:kotlin-reflect:2.0.10")

}