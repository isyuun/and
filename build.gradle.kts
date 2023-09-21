// Top-level build file where you can add configuration options common to all sub-projects/modules.
@file:Suppress("DSL_SCOPE_VIOLATION")

// TODO: Remove once KTIJ-19369 is fixed
plugins {
    id("com.android.application") version "8.1.1" apply false
    ///*alias(libs.plugins.com.android.application) apply false*/
    id("com.android.library") version "8.1.1" apply false
    /*alias(libs.plugins.com.android.library) apply false*/
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    ///*alias(libs.plugins.kotlin.android) apply false*/
    id("com.google.gms.google-services") version "4.3.15" apply false
    ///*alias(libs.plugins.com.google.gms.google.services) apply false*/

    /** IY:플랫폼:샘플스:Define the samples to load */
    ///**id("org.jetbrains.kotlin.kapt") version "1.9.10" apply false*/
    //id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
    //id("com.google.dagger.hilt.android") version "2.48" apply false
    /** IY:플랫폼:샘플스:Define the samples to load */
}
