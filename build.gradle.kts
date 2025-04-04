// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.mikepenz.aboutlibraries.plugin") version "12.0.0-a04"
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}