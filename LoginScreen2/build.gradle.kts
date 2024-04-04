buildscript {
    repositories {
        google() // Add Google Maven repository
        // Add other repositories if needed
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.1") // Add the Google services Gradle plugin
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
}