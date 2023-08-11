// Top-level build file where you can add configuration options common to all sub-projects/modules.
@file:Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    //id("com.android.application") version "8.1.0" apply false
    alias(libs.plugins.com.android.application) apply false
    //id("com.android.library") version "8.1.0" apply false
    alias(libs.plugins.com.android.library) apply false
    //id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    //id("com.google.gms.google-services") version "4.3.15" apply false
    alias(libs.plugins.com.google.gms.google.services) apply false
}