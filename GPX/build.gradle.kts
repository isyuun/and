/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Pet.
 *
 *  Revision History
 *  Author                         Date          Description
 *  --------------------------     ----------    ----------------------------------------
 *  isyuun@care-biz.co.kr             2023. 9. 12.   description...
 */

plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "net.pettip.gpx"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        // -----------------------------------------------------------------------------------------
        // We use the Semantic Versioning (https://semver.org/):
        // -----------------------------------------------------------------------------------------
        vectorDrawables.useSupportLibrary = true

        javaCompileOptions {
            annotationProcessorOptions {
                arguments(mapOf("eventBusIndex" to "eu.basicairdata.graziano.gpslogger.EventBusIndex"))
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // JodaTime for Android
    // https://github.com/dlew/joda-time-android
    implementation("net.danlew:android.joda:2.12.6")        //https://github.com/ticofab/android-gpx-parser
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")    //https://github.com/RideBeeline/android-rx-gpx-writer
    implementation("org.osmdroid:osmdroid-android:6.1.18")  //https://github.com/osmdroid/osmdroid

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit4)
    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}