buildscript {
    val kotlin_version by extra("1.9.24")
    repositories {
        maven { url = uri("https://jitpack.io") }
        mavenCentral()
    }
    dependencies {
        classpath(libs.google.services)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")

        val nav_version = "2.7.7"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    alias(libs.plugins.kotlinAndroid) apply false
}
